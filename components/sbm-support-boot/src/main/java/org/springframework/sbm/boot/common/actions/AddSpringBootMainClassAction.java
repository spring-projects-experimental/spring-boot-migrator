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
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.engine.context.ProjectContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class AddSpringBootMainClassAction extends AbstractAction {

    @Autowired
    @JsonIgnore
    private Configuration configuration;

    @Override
    public void apply(ProjectContext context) {
        List<Module> modules = context.getApplicationModules().getTopmostApplicationModules();

        modules.forEach(module -> {
            applyToModule(module);
        });

    }

    private void applyToModule(Module module) {

        JavaSourceSet mainJavaSourceSet = module.getMainJavaSourceSet();
        JavaSourceLocation location = mainJavaSourceSet.getJavaSourceLocation();

        String className = "SpringBootApp";

        Map<String, Object> params = new HashMap<>();
        params.put("packageName", location.getPackageName());
        params.put("className", className);

        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("boot-app.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String src = writer.toString();
        mainJavaSourceSet.addJavaSource(module.getProjectRootDirectory(), location.getSourceFolder(), src, location.getPackageName());
    }
}
