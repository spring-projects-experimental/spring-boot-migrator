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
package org.springframework.sbm.boot.upgrade_27_30.checks;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.springframework.sbm.boot.asciidoctor.Section;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionRendererTestUtil {
    public static String render(Section section) {
        Version version = new Version("2.3.0");
        Configuration configuration = new Configuration(version);
        try {
            configuration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));
            Map<String, Object> params = new HashMap<>();
            params.put("sections", List.of(section));
//            params.put("className", className);

            StringWriter writer = new StringWriter();
            try {
                Template template = configuration.getTemplate("section.ftl");
                template.process(params, writer);
                String output = writer.toString();
                return output;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
