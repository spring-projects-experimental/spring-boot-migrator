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
import org.openrewrite.ExecutionContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.mule.conditions.MuleConfigFileExist;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.StringProjectResource;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Builder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Setter
public class MigrateMulesoftFile extends AbstractAction {

    @Autowired
    @JsonIgnore
    private Configuration configuration;
    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    @Override
    public void apply(ProjectContext context) {
        List<MuleXml> filteredResources = context.search(new MuleXmlProjectResourceFilter());

        filteredResources.forEach(m -> {
            Map<String, Object> params = new HashMap<>();
            params.put("path", m.getPath());
            params.put("payload", m.getPayload());

            StringWriter writer = new StringWriter();
            try {
                Template template = configuration.getTemplate("spring-integration-template.ftl");
                template.process(params, writer);
                String src = writer.toString();
                StringProjectResource springIntegrationFile = new StringProjectResource(context.getProjectRootDirectory(), context.getProjectRootDirectory().resolve("src/main/resources/spring-integration-flow.xml"), src,
                                                                                        executionContext);
                context.getProjectResources().add(springIntegrationFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return new MuleConfigFileExist().evaluate(context);
    }
}
