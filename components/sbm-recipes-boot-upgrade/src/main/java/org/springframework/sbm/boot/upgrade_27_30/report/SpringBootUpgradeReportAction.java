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
package org.springframework.sbm.boot.upgrade_27_30.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Condition;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Special Action generates a Spring Boot Upgrade report.
 *
 * After being deserialized from {@code YAML}, the Action renders the {@link SpringBootUpgradeReportSection}s
 * as Asciidoctor and adds it into an Asciidoctor report template.
 * The Asciidoctor report is then rendered to HTML and written to {@code filename}.html in the project directory.
 *
 * @author Fabian Krüger
 */
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class SpringBootUpgradeReportAction implements Action {

    private static final String REPORT_DIR = "spring-boot-upgrade-report";

    /**
     * Provides data to render header and footer.
     */
    public interface DataProvider {
        Map<String, Object> getData(ProjectContext context, @Valid List<SpringBootUpgradeReportSection> sections);
    }

    @NotNull
    private Condition condition;

    /**
     * The filename of the generated report, {@code .html} will be appended.
     */
    @NotEmpty
    private String file;
    /**
     * The header of the report
     */
    private String header;
    /**
     * The footer of the report
     */
    private String footer;
    @JsonIgnore
    @Autowired
    private SpringBootUpgradeReportRenderer upgradeReportRenderer;

    @JsonIgnore
    @Autowired
    private SpringBootUpgradeReportFreemarkerSupport freemarkerSupport;

    @JsonIgnore
    @Autowired
    private DataProvider dataProvider = new DataProvider() {
        @Override
        public Map<String, Object> getData(ProjectContext context, @Valid List<SpringBootUpgradeReportSection> sections) {
            return Map.of();
        }
    };

    @Valid
    List<SpringBootUpgradeReportSection> sections;

    @Override
    public String getDescription() {
        return "Creates a Upgrade report for Spring Boot 3.";
    }

    @Override
    public String getDetailedDescription() {
        return "Creates a Upgrade report for Spring Boot 3.";
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public void apply(ProjectContext context) {


        List<String> renderedSections = new ArrayList<>();
        sections.stream()
                .filter(s -> s.shouldRender(context))
                .forEach(section -> {
            renderedSections.add(section.render(context));
        });

        Map<String, Object> data = dataProvider.getData(context, sections);
        String renderedHeader = renderTemplate("header", header, data);

        String renderedFooter = renderTemplate("footer", footer, data);

        Path outputDir = context.getProjectRootDirectory().resolve(REPORT_DIR);
        String filename = file + ".html";
        writeReport(renderedHeader, renderedSections, renderedFooter, outputDir, filename);
    }

    private void writeReport(String renderedHeader, List<String> sections, String renderedFooter, Path outputDir, String filename) {
        String key = "report";
        String content = """
                ${header}
                
                <#list sections as changeSection>
                ${changeSection}
                </#list>
                
                ${footer}
                """;

        Map<String, Object> data = Map.of(
                "header", renderedHeader,
                "sections", sections,
                "footer", renderedFooter
        );

        String renderedTemplate = renderTemplate(key, content, data);

        upgradeReportRenderer.writeReport(renderedTemplate, outputDir, filename);
    }

    private String renderTemplate(String key, String content, Map<String, Object> data) {

        try (StringWriter writer = new StringWriter()) {
            freemarkerSupport.getStringLoader().putTemplate(key, content);
            Template report = freemarkerSupport.getConfiguration().getTemplate(key);
            report.process(data, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void applyInternal(ProjectContext context) {
        apply(context);
    }

    @Override
    public ApplicationEventPublisher getEventPublisher() {
        return null;
    }

    @Override
    public boolean isAutomated() {
        return false;
    }
}
