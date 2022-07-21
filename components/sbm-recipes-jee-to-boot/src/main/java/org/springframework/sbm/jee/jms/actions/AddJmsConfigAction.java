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
package org.springframework.sbm.jee.jms.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.MultiModuleApplicationNotSupportedException;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.api.Type;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AddJmsConfigAction extends AbstractAction {

    @Autowired
    @Setter
    @JsonIgnore
    private Configuration configuration;

    @Override
    public void apply(ProjectContext context) {

        if (context.getApplicationModules().isSingleModuleApplication()) {
            ApplicationModule module = context.getApplicationModules().getRootModule();
            applyToModule(module);
        } else {
            throw new MultiModuleApplicationNotSupportedException("Action can only be applied to applications with single module, but multiple build files were found: ['" + context.getApplicationModules().stream().map(am -> am.getBuildFile().getAbsolutePath().toString()).collect(Collectors.joining("', '")) + "']");
        }

    }

    private void applyToModule(ApplicationModule module) {
        JavaSourceSet mainJavaSourceSet = module.getMainJavaSourceSet();
        JavaSourceLocation location = mainJavaSourceSet.getJavaSourceLocation();

        String className = "JmsConfig";

        Map<String, Object> params = new HashMap<>();
        params.put("packageName", location.getPackageName());
        params.put("className", className);
        Map<String, String> allQueues = findAllQueues(mainJavaSourceSet);
        params.put("queues", allQueues);

        try(StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("jms-config.ftl");
            template.process(params, writer);
            String src = writer.toString();
            mainJavaSourceSet.addJavaSource(module.getProjectRootDirectory(), src, location.getPackageName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Map<String, String> findAllQueues(JavaSourceSet javaSourceSet) {
        Map<String, String> queues = new HashMap<>();
        for (JavaSource js : javaSourceSet.list()) {
            for (Type t : js.getTypes()) {
                for (Member m : t.getMembers()) {
                    if ("javax.jms.Queue".equals(m.getTypeFqName())) {
                        Annotation annotation = m.getAnnotation("javax.annotation.Resource");
                        if (annotation != null) {
                            Expression queueNameExp = annotation.getAttributes().get("name");
                            if (queueNameExp != null) {
                                Expression valueExpr = queueNameExp.getAssignmentRightSide();
                                if (valueExpr != null) {
                                    queues.put(m.getName(), valueExpr.print());
                                }
                            }
                        }
                    }

                }
            }
        }
        return queues;
    }


}
