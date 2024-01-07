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

import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BannerSupportHelper extends SpringBootUpgradeReportSectionHelper<List<String>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";

    private List<Path> foundBanners;

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProjectCondition = new IsSpringBootProject();
        isSpringBootProjectCondition.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBoot3Application = isSpringBootProjectCondition.evaluate(context);
        if(! isSpringBoot3Application) {
            return false;
        }

        foundBanners = context
                .getProjectResources()
                .stream()
                .map(RewriteSourceFileHolder::getAbsolutePath)
                .filter(absolutePath -> absolutePath.toString()
                        .matches(".*banner.(jpg|gif|png)$")
                )
                .collect(Collectors.toList());
        return !foundBanners.isEmpty();
    }

    @Override
    public Map<String, List<String>> getData() {

        return Map.of("files", foundBanners.stream().map(Path::toString).collect(Collectors.toList()));
    }
}
