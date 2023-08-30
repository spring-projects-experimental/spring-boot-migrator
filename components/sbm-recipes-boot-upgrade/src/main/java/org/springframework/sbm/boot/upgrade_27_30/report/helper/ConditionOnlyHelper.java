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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import lombok.Setter;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.Map;

/**
 * Helper for {@link org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection} that takes a {@link Condition} and returns empty map as data.
 *
 * Meant to be used to reuse existing {@link Condition}s for {@link SpringBootUpgradeReportSectionHelper}.
 *
 * @author Fabian Krüger
 */
public class ConditionOnlyHelper extends SpringBootUpgradeReportSectionHelper<String> {

    @Setter
    private Condition condition;

    @Override
    public boolean evaluate(ProjectContext context) {
        return condition.evaluate(context);
    }

    @Override
    public Map<String, String> getData() {
        return Map.of();
    }
}
