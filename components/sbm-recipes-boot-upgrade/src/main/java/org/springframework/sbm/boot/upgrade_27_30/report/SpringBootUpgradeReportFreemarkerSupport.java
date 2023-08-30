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

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author Fabian Krüger
 */
@Component
@Getter
class SpringBootUpgradeReportFreemarkerSupport {

    private final Configuration configuration;
    private final StringTemplateLoader stringLoader;

    public SpringBootUpgradeReportFreemarkerSupport() {

        Version version = new Version("2.3.0");
        configuration = new Configuration(version);
        stringLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(stringLoader);
    }
}
