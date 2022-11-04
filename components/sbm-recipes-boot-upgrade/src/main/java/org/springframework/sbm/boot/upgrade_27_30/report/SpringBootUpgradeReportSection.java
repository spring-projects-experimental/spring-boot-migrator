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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.stringtemplate.v4.ST;

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

    private static final String ls = System.lineSeparator();

    /**
     * Helper acting as {@link Condition} and data provide for a {@link SpringBootUpgradeReportSection}.
     */
    public interface Helper<T> extends Condition {
        /**
         * @return {@code Map<String, T>} the model data for the template.
         */
        Map<String, T> getData();
    }

    public static final String CHANGE_HEADER = "What Changed";
    public static final String AFFECTED = "Why is the application affected";

    public static final String REMEDIATION = "Remediation";

    public boolean shouldRender(ProjectContext context) {
        return helper.evaluate(context);
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
    @NotNull
    private Remediation remediation;
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

    @JsonIgnore
    private Helper helper;
    @JsonIgnore
    @Autowired
    private SpringBootUpgradeReportFreemarkerSupport freemarkerSupport;

    public String render(ProjectContext context) {
        if (getHelper().evaluate(context)) {
            Map<String, Object> params = getHelper().getData();

            try (StringWriter writer = new StringWriter()) {
                String templateContent = buildTemplate();
                renderTemplate(params, writer, templateContent);
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

    private void renderTemplate(Map<String, Object> params, StringWriter writer, String templateContent) throws IOException, TemplateException {
        String templateName = getTitle().replace(" ", "") + UUID.randomUUID();
        freemarkerSupport.getStringLoader().putTemplate(templateName, templateContent);
        Template t = freemarkerSupport.getConfiguration().getTemplate(templateName);
        t.process(params, writer);
    }

    @NotNull
    private String buildTemplate() {
        StringBuilder sb = new StringBuilder();

        renderSectionTitle(sb);
        renderGitHubInfo(sb);
        renderLineBreak(sb);

        renderChangeSubSection(sb);
        renderLineBreak(sb);

        renderAffectedSubSection(sb);
        renderLineBreak(sb);

        renderRemediationSubSection(sb);

        return sb.toString();
    }

    private void renderRemediationSubSection(StringBuilder sb) {
        renderRemediationTitle(sb);
        renderRemediationDescription(sb);
    }

    private void renderRemediationDescription(StringBuilder sb) {
        sb.append(renderRemediation()).append(ls);
    }

    private void renderRemediationTitle(StringBuilder sb) {
        sb.append("==== " + REMEDIATION).append(ls);
    }

    private void renderAffectedSubSection(StringBuilder sb) {
        renderAffectedTitle(sb);
        renderAffectedDescription(sb);
    }

    private void renderAffectedDescription(StringBuilder sb) {
        sb.append(getAffected()).append(ls);
    }

    private void renderChangeSubSection(StringBuilder sb) {
        renderChangeHeader(sb);
        renderChangeDecription(sb);
    }

    private void renderAffectedTitle(StringBuilder sb) {
        sb.append("==== " + AFFECTED).append(ls);
    }

    private void renderChangeDecription(StringBuilder sb) {
        sb.append(getChange()).append(ls);
    }

    private void renderChangeHeader(StringBuilder sb) {
        sb.append("==== " + CHANGE_HEADER);
        renderLineBreak(sb);
    }

    private void renderLineBreak(StringBuilder sb) {
        sb.append(ls);
    }

    private void renderGitHubInfo(StringBuilder sb) {
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
    }

    private void renderSectionTitle(StringBuilder sb) {
        sb.append("=== ").append(title).append(ls);
    }

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

    private String renderRemediation() {
        StringBuilder sb = new StringBuilder();
        sb.append(remediation.getDescription()).append(ls).append(ls);
        if(remediation.getPossibilities().isEmpty()) {
            renderResourcesList(sb, remediation);
        } else {
            remediation.getPossibilities().forEach(p -> renderRemediationPossibility(sb, p));
        }
        return sb.toString();
    }

    private void renderRemediationPossibility(StringBuilder sb, RemediationPossibility p) {
        sb.append("===== ").append(p.getTitle()).append(ls);
        sb.append(p.getDescription()).append(ls).append(ls);
        renderResourcesList(sb, p);
        if(p.getRecipe() != null) {
            sb.append(p.getRecipe()).append(ls).append(ls);
//            ST st = new ST(
//                  """
//                  ++++
//                  <button name="<RECIPE_NAME>" onclick="alert('sending recipe <RECIPE_NAME>')">Apply reecipe '<RECIPE_NAME>'</button>
//                  ++++
//                  """
//            );
//            st.add("RECIPE_NAME", p.getRecipe());
//            sb.append(st.render());
        }
    }

    private void renderResourcesList(StringBuilder sb, ResourceList p) {
        p.getResources().forEach(r -> sb.append("* ").append(r).append(ls));
        if(!p.getResources().isEmpty()) {
            sb.append(ls);
        }
    }


    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public class Author {
        private final String name;
        private final String handle;
    }
}