/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.mule.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.raml.jaxrs.generator.Configuration;
import org.raml.jaxrs.generator.GenerationException;
import org.raml.jaxrs.generator.RamlScanner;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.v08.api.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.common.api.TextResource.TextSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.java.util.JavaSourceUtil;
import org.springframework.sbm.jee.jaxrs.actions.ConvertJaxRsAnnotations;
import org.springframework.sbm.mule.resources.filter.RamlFileProjectResourceFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Slf4j
public class MigrateRamlToSpringMvc extends AbstractAction {

    @Autowired
    @JsonIgnore
    private BasePackageCalculator basePackageCalculator;

    @Override
    public void apply(ProjectContext context) {
        List<TextSource> ramlFiles = context.search(new RamlFileProjectResourceFilter());
        if( ! context.getApplicationModules().isSingleModuleApplication()) {
            log.warn("Action " + getClass().getName() + " is not multi module ready.");
        }

        Module target = context.getApplicationModules().getRootModule();
        ramlFiles.forEach(r -> {
            ramlToJaxRs(context, target, r);
            jaxRsToSpringRest(context);
        });
    }

    private void jaxRsToSpringRest(ProjectContext context) {
        // convert JAX-RS to Spring MVC
        new ConvertJaxRsAnnotations().apply(context);
    }

    private void ramlToJaxRs(ProjectContext context, Module target, TextSource r) {
        new RamlToJaxRsTransformer().transform(context, target, r);
    }

    class RamlToJaxRsTransformer {

        void transform(ProjectContext context, Module target, TextSource r) {
            try {
                String basePackage = target.getMainJavaSourceSet().getJavaSourceLocation().getPackageName();
                Configuration configuration = new Configuration();
                configuration.setModelPackage(basePackage);
                configuration.setResourcePackage(basePackage + ".resource");
                configuration.setSupportPackage(basePackage + ".support");
                configuration.setCopySchemas(false);
                configuration.setOutputDirectory(r.getAbsoluteProjectDir().resolve("src/main/java").normalize().toAbsolutePath().toFile());
//                Found multiple occurrences of org.json.JSONObject on the class path:
//
//                jar:file:/root/.m2/repository/org/json/json/20080701/json-20080701.jar!/org/json/ JSONObject.class
//                jar:file:/root/.m2/repository/com/vaadin/external/google/android-json/0.0.20131108.vaadin1/android-json-0.0.20131108.vaadin1.jar!/org/json/JSONObject.class

//                configuration.setJsonMapper(  AnnotationStyle.valueOf("jackson2".toUpperCase()));

                configuration.setTypeConfiguration(new String[]{"jaxb"});

                ByteArrayInputStream stream = new ByteArrayInputStream(r.print().getBytes(StandardCharsets.UTF_8));
                Path ramlDirectory = r.getAbsolutePath();

                Path tmpDir = Path.of(System.getProperty("java.io.tmpdir")).resolve("sbm-raml-to-jax-rs");
                Files.createDirectories(tmpDir);
                configuration.setOutputDirectory(tmpDir.toFile());

                RamlScanner ramlScanner = new RamlScanner(configuration);

                RamlModelResult result = (new RamlModelBuilder()).buildApi(new InputStreamReader(stream), ramlDirectory + "/");
                if (result.hasErrors()) {
                    throw new GenerationException(result.getValidationResults());
                } else {
                    Api apiV08 = result.getApiV08();
                    if (result.isVersion08() && apiV08 != null) {
                        ramlScanner.handleRamlFile(apiV08, ramlDirectory.toFile());
                    } else {
                        org.raml.v2.api.model.v10.api.Api apiV10 = result.getApiV10();
                        if (result.isVersion10() && apiV10 != null) {
                            ramlScanner.handleRamlFile(apiV10, ramlDirectory.toFile());
                        } else {
                            throw new GenerationException("RAML file is neither v10 nor v08 api file");
                        }
                    }

                }

                JavaSourceSet mainJavaSourceSet = context.getApplicationModules().getRootModule().getMainJavaSourceSet();
                List<String> contents = Files.walk(tmpDir)
                        .filter(f -> f.toAbsolutePath().toString().endsWith(".java"))
                        .map(path -> {
                            try {
                                return Files.readString(path);
                            } catch (IOException e) {
                                // FIXME: add logging
                                e.printStackTrace();
                                return "// Error";
                            }
                        })
                        .collect(Collectors.toList());
                mainJavaSourceSet.addJavaSource(context.getProjectRootDirectory(), Path.of("src/main/java"), contents.toArray(new String[]{}));
                FileUtils.deleteDirectory(tmpDir.toFile());
            } catch (Exception ioException) {
                throw new RuntimeException(ioException);
            }
        }

        private JavaSource addJavaSource(ProjectContext context, JavaSourceSet mainJavaSourceSet, Path j) {
            try {
                String source = Files.readString(j);
                System.out.println(source);
                String packageName = JavaSourceUtil.retrievePackageName(source);
                return mainJavaSourceSet.addJavaSource(context.getProjectRootDirectory(), source, packageName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}