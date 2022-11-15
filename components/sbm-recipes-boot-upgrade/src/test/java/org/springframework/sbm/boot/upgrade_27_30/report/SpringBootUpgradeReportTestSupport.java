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

import lombok.Getter;
import lombok.Setter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.RecipeTestSupport;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.springframework.test.util.ReflectionTestUtils;
import org.stringtemplate.v4.ST;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test helper to verify the markdown that is provided to asciidoctor.
 *
 * @author Fabian Krüger
 */
public class SpringBootUpgradeReportTestSupport {

    public static SectionProjectContext generatedSection(String title) {
        SectionBuilderData builderData = new SectionBuilderData();
        builderData.setTitle(title);
        return new SectionProjectContext(builderData);
    }

    public static SectionProjectContext generatedReport() {
        BuilderData builderData = new ReportBuilderData();
        return new SectionProjectContext(builderData);
    }

    public static class SectionProjectContext {
        private BuilderData builderData;

        public SectionProjectContext(BuilderData builderData) {
            this.builderData = builderData;
        }

        public Assertion fromProjectContext(ProjectContext context) {
            builderData.setContext(context);
            return new Assertion(builderData);
        }

    }

    public static class Assertion {
        private BuilderData builderData;

        public Assertion(BuilderData builderData) {
            this.builderData = builderData;
        }


        public void shouldRenderAs(String expectedOutput) {
            shouldRenderAs(expectedOutput, defaultMap());
        }

        public void shouldRenderAs(String expectedOutput, Map<String, String> templateVariables) {
            String expectedOutputRendered = replacePlaceHolders(expectedOutput, templateVariables);
            Consumer<String> assertion = (s) -> assertThat(s).isEqualTo(expectedOutputRendered);
            verify(assertion);
        }

        public void shouldNotRender() {
            verifyDoesNotRender();
        }

        public void shouldStartWith(String expectedOutput) {
            shouldStartWith(expectedOutput, defaultMap());
        }

        public void shouldStartWith(String expectedOutput, Map<String, String> templateVariables) {
            String expectedOutputRendered = replacePlaceHolders(expectedOutput, templateVariables);
            Consumer<String> assertion = (s) -> assertThat(s).as(TestDiff.of(s, expectedOutputRendered)).startsWith(expectedOutputRendered);
            verify(assertion);
        }


        private Map<String, String> defaultMap() {
            String path = Path
                    .of(".")
                    .toAbsolutePath()
                    .resolve(TestProjectContext.getDefaultProjectRoot()).toString();

            return Map.of("PATH", path);
        }

        private void verifyDoesNotRender() {
            if(SectionBuilderData.class.isInstance(builderData)) {
                SectionBuilderData sectionBuilderData = SectionBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("sbu30-report").get();
                    SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe.getActions().get(0);
                    List<SpringBootUpgradeReportSection> sections = (List<SpringBootUpgradeReportSection>) ReflectionTestUtils.getField(recipe.getActions().get(0), "sections");
                    List<SpringBootUpgradeReportSection> matchingSections = sections
                            .stream()
                            .filter(s -> s.getTitle().equals(builderData.getTitle()))
                            .collect(Collectors.toList());

                    if(matchingSections.size() != 1) {
                        fail("Found " + matchingSections.size() + " Sections with title '" + builderData.getTitle() + "'.");
                    }

                    SpringBootUpgradeReportSection sectionUnderTest = matchingSections.get(0);
                    bruteForceProjectContextIntoProjectContextHolder(builderData.getContext(), action);
                    assertThat(sectionUnderTest.getHelper().evaluate(sectionBuilderData.getContext())).isFalse();
                });
            } else if(ReportBuilderData.class.isInstance(builderData)) {
                ReportBuilderData reportBuilderData = ReportBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("sbu30-report").get();
                    SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe.apply(reportBuilderData.getContext()).get(0);
                    bruteForceProjectContextIntoProjectContextHolder(reportBuilderData.getContext(), action);
                    List<SpringBootUpgradeReportSection> sections = (List<SpringBootUpgradeReportSection>) ReflectionTestUtils.getField(recipe.getActions().get(0), "sections");
                    sections.forEach(sectionUnderTest -> assertThat(sectionUnderTest.getHelper().evaluate(reportBuilderData.getContext())).isFalse());
                });
            }
        }

        /**
         * Another nasty hack required to make the ProjectContext available in ProjectContextHolder which is required by the
         * hacked implementation of Spring Upgrade report web application.
         * The {@code SpringBootUpgradeReportFileSystemRenderer} accesses the {@code ProjectContext} through
         * {@ProjectContextHolder} but its set in {@code ScanShellCommand} which is not available here.
         */
        private void bruteForceProjectContextIntoProjectContextHolder(ProjectContext context, SpringBootUpgradeReportAction action) {
            ProjectContextHolder contextHolder = new ProjectContextHolder();
            contextHolder.setProjectContext(context);
            ReflectionTestUtils.setField(action.getUpgradeReportProcessor(), "contextHolder", contextHolder);
        }

        private void verify(Consumer<String> assertion) {
            if(ReportBuilderData.class.isInstance(builderData)) {
                ReportBuilderData reportBuilderData = ReportBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("sbu30-report").get();
                    SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe.getActions().get(0);
                    bruteForceProjectContextIntoProjectContextHolder(builderData.getContext(), action);
//                    ReflectionTestUtils.setField(action, "upgradeReportProcessor", (SpringBootUpgradeReportFileSystemRenderer) s -> assertion.accept(s));
                    action.apply(reportBuilderData.getContext());
                });
            } else if(SectionBuilderData.class.isInstance(builderData)) {
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("sbu30-report").get();
                    SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe.getActions().get(0);
                    bruteForceProjectContextIntoProjectContextHolder(builderData.getContext(), action);
                    List<SpringBootUpgradeReportSection> sections = (List<SpringBootUpgradeReportSection>) ReflectionTestUtils.getField(recipe.getActions().get(0), "sections");
                    List<SpringBootUpgradeReportSection> matchingSections = sections
                            .stream()
                            .filter(s -> s.getTitle().equals(builderData.getTitle()))
                            .collect(Collectors.toList());

                    if(matchingSections.size() != 1) {
                        fail("Found " + matchingSections.size() + " Sections with title '" + builderData.getTitle() + "'.");
                    }

                    SpringBootUpgradeReportSection sectionUnderTest = matchingSections.get(0);
                    action.apply(builderData.getContext());
                    String renderedSection = sectionUnderTest.render(builderData.getContext());
                    String renderedSectionWithoutButtonCode = replaceRecipeButtonCodeFromExpectedOutput(sectionUnderTest, renderedSection);

                    assertion.accept(renderedSectionWithoutButtonCode);
                });
            }
        }

        /**
         * Another hack, removing the expected button code added to the Asciidoc to free tests from asserting invisible
         * code of buttons to apply a recipe.
         */
        private String replaceRecipeButtonCodeFromExpectedOutput(SpringBootUpgradeReportSection sectionUnderTest, String renderedSection) {
            StringBuilder sb = new StringBuilder();
            List<String> buttonCodes = new ArrayList<>();
            if(sectionUnderTest.getRemediation().getPossibilities().isEmpty()) {
                String recipe = sectionUnderTest.getRemediation().getRecipe();
                if(recipe != null) {
                    String target = """
                                                                                    
                              ++++
                              <div class="run-a-recipe" recipe="<RECIPE>">
                              </div>
                              ++++
                                                                            
                              """;
                    buttonCodes.add(target.replace("<RECIPE>", recipe));
                }
            } else {
                buttonCodes = sectionUnderTest
                        .getRemediation()
                        .getPossibilities()
                        .stream()
                        .filter(p -> p.getRecipe() != null)
                        .map(RemediationPossibility::getRecipe)
                        .map(recipe -> {
                            String target = """
                                                                                    
                              ++++
                              <div class="run-a-recipe" recipe="<RECIPE>">
                              </div>
                              ++++
                                                                            
                              """;
                            return target.replace("<RECIPE>", recipe);
                        })
                        .collect(Collectors.toList());
            }

            for(String buttonCode : buttonCodes) {
                renderedSection = renderedSection.replace(buttonCode, "");
            }
            return renderedSection;
        }

        private void withRecipes(Consumer<Recipes> recipesConsumer) {
            RecipeTestSupport.testRecipe(
                    Path.of("recipes/27_30/report/sbu30-report.yaml"), recipesConsumer,
                    SpringBootUpgradeReportActionDeserializer.class,
                    SpringBootUpgradeReportFreemarkerSupport.class,
                    SpringBootUpgradeReportFileSystemRenderer.class,
                    SpringBootUpgradeReportDataProvider.class
            );
        }

        private String replacePlaceHolders(String expectedOutput, Map<String, String> templateVariables) {
            ST st = new ST(expectedOutput);
            templateVariables.entrySet().stream().forEach(e -> st.add(e.getKey(), e.getValue()));
            return st.render();
        }
    }

    @Getter
    @Setter
    private static class BuilderData {
        private ProjectContext context;
        private String title;
    }
    private static class SectionBuilderData extends BuilderData {
    }

    private static class ReportBuilderData extends BuilderData {
    }
}
