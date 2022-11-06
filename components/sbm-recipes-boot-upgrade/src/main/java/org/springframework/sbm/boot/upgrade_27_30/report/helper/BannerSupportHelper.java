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

package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BannerSupportHelper implements SpringBootUpgradeReportSection.Helper<List<String>> {

    private List<Path> foundBanners;

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
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
    public Map<String, List<String>> getData(ProjectContext context) {

        return Map.of("files", foundBanners.stream().map(Path::toString).collect(Collectors.toList()));
    }
}
