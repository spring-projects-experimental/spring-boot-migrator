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
import freemarker.core.ParseException;
import freemarker.template.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A section in a generated report for Spring Boot 3 upgrade, rendered as Asciidoc from a freemarker template string.
 * The {@link Helper} is condition and evaluates if the section should be rendered.
 * If the section should be rendered, the {@link Helper} provides the data extracted from {@link ProjectContext} to render the template.
 *
 * @author Fabian Krüger
 */
@Getter
@Setter
public class SpringBootUpgradeReportSection {

    public static final String CHANGE = "What Changed";
    public static final String AFFECTED = "Why is the application affected";
    public static final String REMEDIATION = "Remediation";

    public boolean shouldRender(ProjectContext context) {
        return helper.evaluate(context);
    }

    /**
     * Helper acting as {@link Condition} and data provide for a {@link SpringBootUpgradeReportSection}.
     */
    public interface Helper<T> extends Condition {
        /**
         * @return {@code Map<String, T>} the model data for the template.
         */
        Map<String, T> getData(ProjectContext context);
    }

    /**
     * Section title
     */
    @NotEmpty
    private String title;
    /**
     * Describes the change, e.g a section from Release Notes.
     */
    @NotEmpty
    private String change;
    /**
     * Describes why the scanned application is affected by this change.
     */
    @NotEmpty
    private String affected;
    /**
     * Describes required changes to the scanned application.
     */
    @NotEmpty
    private String remediation;
    /**
     * The id of the GitHub issue to this report section.
     */
    @NotNull
    private Integer gitHubIssue;
    /**
     * Contributors with pattern {@code Given Name[@githubHandle]}.
     */
    @NotNull
    private Set<String> contributors;
    /**
     * The name of the recipe for automated migration or {@code null} of none exist.
     */
    @Nullable
    private String recipe;

    @JsonIgnore
    private Helper helper;
    @JsonIgnore
    @Autowired
    private SpringBootUpgradeReportFreemarkerSupport freemarkerSupport;

    public List<Author> getAuthors() {
        return contributors.stream()
                .map(c -> {
                    Matcher matcher = Pattern.compile("(.*)\\[(.*)\\]").matcher(c);
                    if(matcher.find()) {
                        String name = matcher.group(1);
                        String handle = matcher.group(2).replace("@", "");
                        return new Author(name, handle);
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    public String render(ProjectContext context) {
        if (getHelper().evaluate(context)) {
            Map<String, Object> params = new HashMap<>();
            if (getHelper() != null) {
                params = getHelper().getData(context);
            }

            try (StringWriter writer = new StringWriter()) {
                String templateContent = buildTemplate();

                freemarkerSupport.getStringLoader().putTemplate("section", templateContent);
                Template t = freemarkerSupport.getConfiguration().getTemplate("section");
                t.process(params, writer);
                return writer.toString();
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            } catch (TemplateNotFoundException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (MalformedTemplateNameException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("Could not render Sectipn '"+ getTitle()+"', evaluating the context returned false");
    }

    @NotNull
    private String buildTemplate() {
        StringBuilder sb = new StringBuilder();

        String ls = System.lineSeparator();

        sb.append("=== ").append(getTitle()).append(ls);
        if(gitHubIssue != null) {
            sb.append("Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/").append(gitHubIssue).append("[#").append(gitHubIssue).append("]");
        }
        if(contributors != null && gitHubIssue != null) {
            sb.append(", ");
        } else {
            sb.append(ls);
        }
        if(contributors != null) {
            List<Author> authors = getAuthors();
            sb.append("Contributors: ");
            authors.stream()
                    .forEach(a -> {
                        sb.append("https://github.com/").append(a.getHandle()).append("[@").append(a.getHandle()).append("^, role=\"ext-link\"]");
                    });
            sb.append(ls);
        }
        sb.append(ls)
        .append("==== " + CHANGE).append(ls)
        .append(getChange()).append(ls)
        .append(ls)
        .append("==== " + AFFECTED).append(ls)
        .append(getAffected()).append(ls)
        .append(ls)
        .append("==== " + REMEDIATION).append(ls)
        .append(getRemediation()).append(ls)
        .append(ls);




        String templateContent = sb.toString();

        return templateContent;
    }


    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public class Author {
        private final String name;
        private final String handle;
    }
}