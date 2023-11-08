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
package org.springframework.sbm.boot.upgrade_27_30.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.template.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A section in a generated report for Spring Boot 3 upgrade, rendered as Asciidoc from a freemarker template string.
 * The {@link SpringBootUpgradeReportSectionHelper Helper} is condition and evaluates if the section should be rendered.
 * If the section should be rendered, the {@link SpringBootUpgradeReportSectionHelper Helper} provides the data extracted
 * from {@link ProjectContext} to render the template.
 *
 * @author Fabian Krüger
 */
@Getter
@Setter
public class SpringBootUpgradeReportSection {

    public static final String CHANGE_HEADER = "What Changed";
    public static final String AFFECTED = "Why is the application affected";
    public static final String REMEDIATION = "Remediation";
    private static final String GITHUB_ISSUE = "**Issue:** https://github.com/spring-projects-experimental/spring-boot-migrator/issues/%1$d[#%1$d^, role=\"ext-link\"]  + %n";
    private static final String GITHUB_CONTRIBUTOR = "https://github.com/%1$s[@%1$s^, role=\"ext-link\"]";
    private static final String ls = System.lineSeparator();

    public static final String BUTTON_CODE = """
            ++++
            <div class="run-a-recipe" recipe="<RECIPE>">
            </div>
            ++++
            """.replace("\n", System.lineSeparator());

    /**
     * The spring project(s)/modules this change comes from.
     *
     * e.g. {@code spring-boot} and {@code actuator}
     */
    private List<String> projects;
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

    @NotNull
    private SpringBootUpgradeReportSectionHelper<?> helper;

    @JsonIgnore
    @Autowired
    private SpringBootUpgradeReportFreemarkerSupport freemarkerSupport;

    public boolean shouldRender(ProjectContext context) {
        return helper.evaluate(context);
    }

    public String render(ProjectContext context) {
        if (getHelper().evaluate(context)) {
            Map<String, ?> params = getHelper().getData();

            try (StringWriter writer = new StringWriter()) {
                String templateContent = buildTemplate();
                renderTemplate(params, writer, templateContent);
                return writer.toString();
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new IllegalArgumentException("Could not render Section '%s', evaluating the context returned false".formatted(getTitle()));
    }

    private void renderTemplate(Map<String, ?> params, StringWriter writer, String templateContent) throws IOException, TemplateException {
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
        sb.append("==== %s%n".formatted(REMEDIATION));
    }

    private void renderAffectedSubSection(StringBuilder sb) {
        renderAffectedTitle(sb);
        renderAffectedDescription(sb);
    }

    private void renderAffectedDescription(StringBuilder sb) {
        sb.append("%s%n".formatted(getAffected()));
    }

    private void renderChangeSubSection(StringBuilder sb) {
        renderChangeHeader(sb);
        renderChangeDecription(sb);
    }

    private void renderAffectedTitle(StringBuilder sb) {
        sb.append("==== %s%n".formatted(AFFECTED));
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

    void renderGitHubInfo(StringBuilder sb) {
        if (gitHubIssue != null) {
            sb.append(GITHUB_ISSUE.formatted(gitHubIssue));
        }
        if (contributors != null) {
            List<Author> authors = getAuthors();
            sb.append("**Contributors:** ");
            String authorsString = authors.stream().map(a -> GITHUB_CONTRIBUTOR.formatted(a.getHandle())).collect(Collectors.joining(", "));
            sb.append(authorsString).append("%s + %n".formatted(authorsString));
        }
        if (projects != null) {
            String projectsList = String.join(", ", projects);
            sb.append("**Projects:** %s%n".formatted(projectsList));
        }
    }

    private void renderSectionTitle(StringBuilder sb) {
        sb.append("=== %s%n".formatted(title));
    }

    public List<Author> getAuthors() {
        if(contributors == null) {
            return List.of();
        }

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
        if (remediation.getDescription() != null) {
            sb.append("%s%n".formatted(remediation.getDescription()));
        }
        sb.append(ls);
        if (remediation.getPossibilities().isEmpty()) {
            renderResourcesList(sb, remediation);
            renderRecipeButton(sb, remediation.getRecipe());
        } else {
            remediation.getPossibilities().forEach(p -> renderRemediationPossibility(sb, p));
        }
        return sb.toString();
    }

    private void renderRemediationPossibility(StringBuilder sb, RemediationPossibility p) {
        sb.append("===== %s%n%s%n%n".formatted(p.getTitle(), p.getDescription()));
        renderResourcesList(sb, p);
        renderRecipeButton(sb, p.getRecipe());
    }

    private void renderRecipeButton(StringBuilder sb, String recipe) {
        if (recipe != null && !recipe.isEmpty()) {
            sb.append(ls).append(ls);
            /*
            <!--
                    <form name="apply-<RECIPE>-form" action="http://localhost:8080/spring-boot-upgrade" method="post">
                    <input type="hidden" name="recipeNames[0]" value="<RECIPE>" />
                    <button name="<RECIPE>" type="submit"  class="recipeButton">Run Recipe</button>

                    </form>
                    -->

                    <button type="button" class="btn btn-primary recipeButton" onclick="applyRecipes('<RECIPE>')">
                        Run All Recipes
                        <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>
                        <span class="visually-hidden">Loading...</span>
                    </button>
            */
            String buttonCode = BUTTON_CODE.replace("<RECIPE>", recipe);
            sb.append(buttonCode);
        }
    }

    private void renderResourcesList(StringBuilder sb, ResourceList p) {
        p.getResources().forEach(r -> sb.append("* %s%n".formatted(r)));
        if (!p.getResources().isEmpty()) {
            sb.append(ls);
        }
    }


    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class Author {
        private final String name;
        private final String handle;
    }
}