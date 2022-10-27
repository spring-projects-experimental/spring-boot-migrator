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
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.test.RecipeTestSupport;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.springframework.test.util.ReflectionTestUtils;
import org.stringtemplate.v4.ST;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
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
            shouldRenderAs(expectedOutput, Map.of());
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
            shouldStartWith(expectedOutput, Map.of());
        }

        public void shouldStartWith(String expectedOutput, Map<String, String> templateVariables) {
            String expectedOutputRendered = replacePlaceHolders(expectedOutput, templateVariables);
            Consumer<String> assertion = (s) -> assertThat(s).as(TestDiff.of(s, expectedOutputRendered)).startsWith(expectedOutputRendered);
            verify(assertion);
        }

        private void verifyDoesNotRender() {
            if(SectionBuilderData.class.isInstance(builderData)) {
                SectionBuilderData sectionBuilderData = SectionBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("boot-2.7-3.0-upgrade-report2").get();
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


                    action.apply(builderData.getContext());
                    assertThat(sectionUnderTest.getHelper().evaluate(sectionBuilderData.getContext())).isFalse();
                });
            } else if(ReportBuilderData.class.isInstance(builderData)) {
                ReportBuilderData reportBuilderData = ReportBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("boot-2.7-3.0-upgrade-report2").get();
                    Action action = recipe.apply(reportBuilderData.getContext()).get(0);
                    List<SpringBootUpgradeReportSection> sections = (List<SpringBootUpgradeReportSection>) ReflectionTestUtils.getField(recipe.getActions().get(0), "sections");
                    sections.forEach(sectionUnderTest -> assertThat(sectionUnderTest.getHelper().evaluate(reportBuilderData.getContext())).isFalse());
                });
            }
        }

        private void verify(Consumer<String> assertion) {
            if(ReportBuilderData.class.isInstance(builderData)) {
                ReportBuilderData reportBuilderData = ReportBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("boot-2.7-3.0-upgrade-report2").get();
                    SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe.getActions().get(0);
                    ReflectionTestUtils.setField(action, "upgradeReportRenderer",
                                                 new SpringBootUpgradeReportRenderer() {
                                                     @Override
                                                     public String renderReport(String s) {
                                                         assertion.accept(s);
                                                         return super.renderReport(s);
                                                     }
                                                 });
                    action.apply(reportBuilderData.getContext());
                });
            } else if(SectionBuilderData.class.isInstance(builderData)) {
                SectionBuilderData sectionBuilderData = SectionBuilderData.class.cast(builderData);
                withRecipes(recipes -> {
                    Recipe recipe = recipes.getRecipeByName("boot-2.7-3.0-upgrade-report2").get();
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


                    action.apply(builderData.getContext());
                    String renderedSection = sectionUnderTest.render(builderData.getContext());
                    assertion.accept(renderedSection);
                });
            }
        }

        private void withRecipes(Consumer<Recipes> recipesConsumer) {
            RecipeTestSupport.testRecipe(
                    Path.of("recipes/boot-new-report.yaml"), recipesConsumer,
                    SpringBootUpgradeReportActionDeserializer.class,
                    SpringBootUpgradeReportFreemarkerSupport.class,
                    SpringBootUpgradeReportRenderer.class,
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
