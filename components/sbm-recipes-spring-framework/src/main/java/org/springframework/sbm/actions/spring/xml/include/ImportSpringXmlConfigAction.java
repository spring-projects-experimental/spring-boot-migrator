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
package org.springframework.sbm.actions.spring.xml.include;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.sbm.common.util.LinuxWindowsPathUnifier;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.build.MultiModuleApplicationNotSupportedException;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.XmlSpringBeanConfigurationFilter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportSpringXmlConfigAction extends AbstractAction {

    @Setter
    @Autowired
    @JsonIgnore
    private Configuration freemarkerConf;
    
    @Override
    public void apply(ProjectContext context) {
        if (context.getApplicationModules().isSingleModuleApplication()) {
            List<RewriteSourceFileHolder> xmlFiles = getResourcesToImport(context);
            Module module = context.getApplicationModules().getRootModule();
            applyToModule(module, xmlFiles);
        } else {
            throw new MultiModuleApplicationNotSupportedException("Action can only be applied to applications with single module.");
        }
    }

    private void applyToModule(Module module, List<RewriteSourceFileHolder> xmlFiles) {

        JavaSourceSet mainJavaSourceSet = module.getMainJavaSourceSet();
        JavaSourceLocation sourceFileLocation = mainJavaSourceSet.getJavaSourceLocation();
        Path javaSourceFolder = sourceFileLocation.getSourceFolder();
        String pkg = sourceFileLocation.getPackageName();

        List<Path> classpathRoots = new ArrayList<>();
        classpathRoots.addAll(module.getBuildFile().getSourceFolders());
        classpathRoots.addAll(module.getBuildFile().getResourceFolders());

        String relativePaths = xmlFiles.stream()
                .map(xmlFile -> makeRelativeTo(xmlFile.getAbsolutePath(), classpathRoots))
                .sorted()
                .map(rp -> "\"classpath:"+rp+"\"").collect(Collectors.joining(", ")); // avoid 'randomized' behavior.
        String resourcesString = relativePaths;

        Map<String, Object> params = new HashMap<>();
        params.put("packageName", pkg);
        params.put("commaSeparatedResources", resourcesString);

        StringWriter writer = new StringWriter();
        try {
            Template template = freemarkerConf.getTemplate("import-resources.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String src = writer.toString();
        mainJavaSourceSet.addJavaSource(module.getProjectRootDirectory(), javaSourceFolder, src, pkg);
    }

    private String makeRelativeTo(Path absolutePath, List<Path> classpathRoots) {
        // FIXME: can just return sourcePath if sourcePath becomes relative path to projectRootDir
        Assert.isTrue(absolutePath.isAbsolute(), "must be absolute");
        for (Path cpr : classpathRoots) {
            if (absolutePath.startsWith(cpr)) {
                Path relativePath = cpr.relativize(absolutePath);
                return new LinuxWindowsPathUnifier().unifyPath(relativePath.toString());
            }
        }
        throw new RuntimeException(String.format("Absolute path '%s' is not contained in any classpath root", absolutePath));
    }

    private List<RewriteSourceFileHolder> getResourcesToImport(ProjectContext module) {
        return module.search(new XmlSpringBeanConfigurationFilter());
    }
    
}
