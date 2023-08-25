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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.boot.upgrade_27_30.report.helper.BannerSupportHelper;
import org.springframework.sbm.boot.upgrade_27_30.report.helper.ConditionOnlyHelper;
import org.springframework.sbm.boot.upgrade_27_30.report.yaml.SpringBootUpgradeReportSectionHelperDeserializer;
import org.springframework.sbm.boot.upgrade_27_30.report.helper.IsSpring27Or30ProjectHelper;
import org.springframework.sbm.boot.upgrade_27_30.report.yaml.SpringBootUpgradeReportActionDeserializer;
import org.springframework.sbm.boot.upgrade_27_30.report.yaml.SpringBootUpgradeReportYamlDeserializationConfiguration;
import org.springframework.sbm.common.migration.conditions.TrueCondition;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = {
        SpringBootUpgradeReportYamlDeserializationConfiguration.class,
        YamlObjectMapperConfiguration.class,
        SpringBootUpgradeReportDataProvider.class,
        ProjectContextHolder.class,
        SpringBootUpgradeReportFreemarkerSupport.class,
        SpringBootUpgradeReportFileSystemRenderer.class,
        SpringBootUpgradeReportActionDeserializer.class,
        ActionDeserializerRegistry.class
})
public class SpringBootUpgradeReportDeserializationTest {

    @Autowired
    private ObjectMapper yamlObjectMapper;

    @Test
    void deserializeAction() throws IOException, URISyntaxException {

        String yaml = """
                - name: sbu30-report
                  description: Create a report for Spring Boot Upgrade from 2.7.x to 3.0.x
                  condition:
                    type: org.springframework.sbm.boot.common.conditions.IsSpringBootProject
                    versionPattern: "2\\\\.7\\\\..*"
                                
                  actions:
                                
                    - type: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction
                      file: report
                      condition:
                        type: org.springframework.sbm.common.migration.conditions.TrueCondition
                                
                      dataProvider: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportDataProvider
                                
                      header: |-
                        The header...
                        with two lines
                      sections:
                                
                        - title: Upgrade Dependencies
                          helper: org.springframework.sbm.boot.upgrade_27_30.report.helper.IsSpring27Or30ProjectHelper
                          change: |-
                            Spring Boot 3.0 upgraded many used dependencies.\s
                            Also, dependencies previously in the `javax` packages use the new `jakarta` packages now.
                            WARNING: THis is a dummy and needs to be replaced with the matching changes from the release notes!
                          affected: |-
                            List the found dependencies here?
                          remediation:
                            description: |-
                              A comprehensive list of affected dependencies and their new replacements
                            recipe: sbu30-upgrade-dependencies
                          contributors:
                            - "Fabian Krüger[@fabapp2]"

                      footer: |-
                        We want to say thank you to all Contributors!
                        Generated by Spring Boot Migrator (experimental)              
                """;

        Recipe[] recipe = yamlObjectMapper.readValue(yaml, Recipe[].class);

        assertThat(recipe[0].getActions()).hasSize(1);
        assertThat(recipe[0].getActions().get(0)).isInstanceOf(SpringBootUpgradeReportAction.class);
        SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) recipe[0].getActions().get(0);
        assertThat(action.getCondition()).isInstanceOf(TrueCondition.class);
        assertThat(action.getDataProvider()).isInstanceOf(SpringBootUpgradeReportDataProvider.class);
        assertThat(action.getHeader()).isEqualTo("""
                                                 The header...
                                                 with two lines""");
        assertThat(action.getFooter()).isEqualTo("""
                                                 We want to say thank you to all Contributors!
                                                 Generated by Spring Boot Migrator (experimental)""");

        assertThat(action.getSections()).hasSize(1);
        SpringBootUpgradeReportSection section = (SpringBootUpgradeReportSection) action.getSections().get(0);
        assertThat(section.getTitle()).isEqualTo("Upgrade Dependencies");
        assertThat(section.getHelper()).isInstanceOf(IsSpring27Or30ProjectHelper.class);
        assertThat(section.getChange()).isEqualTo("""
                                                  Spring Boot 3.0 upgraded many used dependencies.\s
                                                  Also, dependencies previously in the `javax` packages use the new `jakarta` packages now.
                                                  WARNING: THis is a dummy and needs to be replaced with the matching changes from the release notes!""");
        assertThat(section.getAffected()).isEqualTo("""
                                                    List the found dependencies here?""");
        assertThat(section.getContributors()).hasSize(1);
        assertThat(section.getContributors().iterator().next()).isEqualTo("Fabian Krüger[@fabapp2]");

        assertThat(section.getRemediation()).isInstanceOf(Remediation.class);
        Remediation remediation = section.getRemediation();
        assertThat(remediation.getDescription()).isEqualTo("A comprehensive list of affected dependencies and their new replacements");
        assertThat(remediation.getRecipe()).isEqualTo("sbu30-upgrade-dependencies");
    }


    @Test
    void conditionOnlyHelper() throws JsonProcessingException {

        String yaml =
                """
                type: org.springframework.sbm.boot.upgrade_27_30.report.helper.ConditionOnlyHelper
                condition:
                  type: org.springframework.sbm.java.migration.conditions.HasImportStartingWith
                  value: "a.b"
                """;

        AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule("HelperModule");
        module.addDeserializer(SpringBootUpgradeReportSectionHelper.class, new SpringBootUpgradeReportSectionHelperDeserializer());
        module.addDeserializer(Condition.class, new ConditionDeserializer(objectMapper, beanFactory));
        objectMapper.registerModule(module);

        SpringBootUpgradeReportSectionHelper helper = objectMapper.readValue(yaml, SpringBootUpgradeReportSectionHelper.class);

        assertThat(helper).isInstanceOf(ConditionOnlyHelper.class);
        ConditionOnlyHelper concreteHelper = (ConditionOnlyHelper) helper;
        HasImportStartingWith condition = (HasImportStartingWith) ReflectionTestUtils.getField(concreteHelper, "condition");
        assertThat(condition).hasFieldOrPropertyWithValue("value", "a.b");
    }


    @Setter
    @Getter
    public static class HoldingHelper {
        private SpringBootUpgradeReportSectionHelper<String> helper;
    }

    @Test
    void standardHelper() throws JsonProcessingException {
        String yaml =
                """
                helper: org.springframework.sbm.boot.upgrade_27_30.report.helper.BannerSupportHelper
                """;

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule("HelperModule");
        module.addDeserializer(SpringBootUpgradeReportSectionHelper.class, new SpringBootUpgradeReportSectionHelperDeserializer());
        objectMapper.registerModule(module);

        HoldingHelper helper = objectMapper.readValue(yaml, HoldingHelper.class);
        assertThat(helper).isNotNull();
        assertThat(helper.getHelper()).isInstanceOf(BannerSupportHelper.class);
    }

    @Test
    void sectionCanHaveConditionOnly() throws JsonProcessingException {
        String yaml = """
                      type: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction
                      file: report
                      condition:
                        type: org.springframework.sbm.boot.common.conditions.IsSpringBootProject
                        versionPattern: "2\\\\.7\\\\..*"
                      dataProvider: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportDataProvider
                      header: |-
                        The header...
                        with two lines
                      sections:
                        - title: Title 1
                          helper:
                            type: org.springframework.sbm.boot.upgrade_27_30.report.helper.ConditionOnlyHelper
                            condition:
                              type: org.springframework.sbm.java.migration.conditions.HasImportStartingWith
                              value: "a.b"
                          change: |-
                            The change
                          affected: |-
                            List the found dependencies here?
                          remediation:
                            description: |-
                              A comprehensive list of affected dependencies and their new replacements
                            recipe: sbu30-upgrade-dependencies
                          contributors:
                            - "Fabian Krüger[@fabapp2]"
                      """;

        AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule("HelperModule");
        module.addDeserializer(SpringBootUpgradeReportSectionHelper.class, new SpringBootUpgradeReportSectionHelperDeserializer());
        module.addDeserializer(Condition.class, new ConditionDeserializer(objectMapper, beanFactory));
        objectMapper.registerModule(module);

        SpringBootUpgradeReportAction action = objectMapper.readValue(yaml, SpringBootUpgradeReportAction.class);
        assertThat(action.getSections().get(0).getHelper()).isInstanceOf(ConditionOnlyHelper.class);
        ConditionOnlyHelper helper = (ConditionOnlyHelper) action.getSections().get(0).getHelper();
        assertThat(helper.getData()).hasSize(0);
        HasImportStartingWith condition = (HasImportStartingWith) ReflectionTestUtils.getField(helper, "condition");
        assertThat(condition.getValue()).isEqualTo("a.b");
    }

    @Test
    void sectionCanHaveConditionOnly2() throws JsonProcessingException {
        String yaml = """
                      type: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction
                      file: report
                      condition:
                        type: org.springframework.sbm.boot.common.conditions.IsSpringBootProject
                        versionPattern: "2\\\\.7\\\\..*"
                      dataProvider: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportDataProvider
                      header: |-
                        The header...
                        with two lines
                      sections:
                        - title: Title 2
                          helper: org.springframework.sbm.boot.upgrade_27_30.report.helper.BannerSupportHelper
                          change: |-
                            The change 2
                          affected: |-
                            Affected 2
                          remediation:
                            description: |-
                              Remediation description 2
                            recipe: sbu30-upgrade-dependencies
                          contributors:
                            - "Fabian Krüger[@fabapp2]"                            
                      """;

        AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule("HelperModule");
        module.addDeserializer(SpringBootUpgradeReportSectionHelper.class, new SpringBootUpgradeReportSectionHelperDeserializer());
        module.addDeserializer(Condition.class, new ConditionDeserializer(objectMapper, beanFactory));
//        module.addDeserializer(SpringBootUpgradeReportAction.class, new SpringBootUpgradeReportActionDeserializer(objectMapper, beanFactory));
        objectMapper.registerModule(module);

        SpringBootUpgradeReportAction action = objectMapper.readValue(yaml, SpringBootUpgradeReportAction.class);

        assertThat(action.getSections().get(0).getHelper()).isInstanceOf(BannerSupportHelper.class);
    }


}
