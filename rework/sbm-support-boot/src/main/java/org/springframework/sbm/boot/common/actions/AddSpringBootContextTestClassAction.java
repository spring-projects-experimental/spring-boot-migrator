/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.boot.common.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSpringBootContextTestClassAction extends AbstractAction {

    @Autowired
    @Setter
    @JsonIgnore
    private Configuration configuration;

    @Autowired
    @Setter
    @JsonIgnore
    private SbmApplicationProperties sbmApplicationProperties;

    @Override
    public void apply(ProjectContext context) {
        List<Module> modules = context.getApplicationModules().getTopmostApplicationModules();

        modules.forEach(module -> {
            handleModule(module);
        });
    }

    private void handleModule(Module module) {

        JavaSourceSet mainJavaSourceSet = module.getMainJavaSourceSet();
        JavaSourceSet testJavaSourceSet = module.getTestJavaSourceSet();
        JavaSourceLocation mainJavaSourceLocation = mainJavaSourceSet.getJavaSourceLocation();
        JavaSourceLocation testJavaSourceLocation = testJavaSourceSet.getJavaSourceLocation();

        String className = "SpringBootAppTest";

        Map<String, Object> params = new HashMap<>();
        String packageName = testJavaSourceLocation.getPackageName();
        if (packageName == null || packageName.equals(sbmApplicationProperties.getDefaultBasePackage())) {
            packageName = mainJavaSourceLocation.getPackageName();
        }

        params.put("packageName", packageName);
        params.put("className", className);

        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("boot-app-test.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String src = writer.toString();
        testJavaSourceSet.addJavaSource(module.getProjectRootDir(), testJavaSourceLocation.getSourceFolder(), src, packageName);
    }

}
