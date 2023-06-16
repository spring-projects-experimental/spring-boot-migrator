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
package org.springframework.sbm.boot.upgrade_24_25.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openrewrite.ExecutionContext;
import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade.common.UpgradeReportUtil;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.StringProjectResource;
import freemarker.template.Configuration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.boot.upgrade_24_25.report.Boot_24_25_Introduction;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Boot_24_25_UpgradeReportAction extends AbstractAction {

    @Autowired
    @JsonIgnore
    private Configuration configuration;

    @Autowired
    @JsonIgnore
    private List<UpgradeSectionBuilder> upgradeSectionBuilders = new ArrayList<>();

    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    @Override
    public void apply(ProjectContext projectContext) {

        final List<Section> sections = upgradeSectionBuilders.stream()
            .filter(b -> b.isApplicable(projectContext))
            .map(b -> b.build(projectContext))
            .collect(Collectors.toList());

        Map<String, Object> params = new HashMap<>();
        Section introductionSection = new Boot_24_25_Introduction().build(projectContext);
        params.put("introductionSection", introductionSection);
        params.put("changeSections", sections);
        String markdown = UpgradeReportUtil.renderMarkdown(params, configuration);
        String html = UpgradeReportUtil.renderHtml(markdown);
        Path htmlPath = projectContext.getProjectRootDirectory().resolve(Path.of("Upgrade-Spring-Boot-2.4-to-2.5.html"));
        projectContext.getProjectResources().add(new StringProjectResource(projectContext.getProjectRootDirectory(), htmlPath, html,
                                                                           executionContext));
    }




    @Override
    public boolean isApplicable(ProjectContext context) {
        // Verify it's a 2.4.x Spring Boot project
        IsSpringBootProject isSpringBootProject = new IsSpringBootProject();
        isSpringBootProject.setVersionPattern("2\\.4\\..*");

        return isSpringBootProject.evaluate(context);
    }

    @Getter
    public class JpaRepositoryDefinition {
        private final String implementedType;
        private final JavaSource js;

        public JpaRepositoryDefinition(String implementedType, JavaSource js) {
            this.implementedType = implementedType;
            this.js = js;
        }
    }

}
