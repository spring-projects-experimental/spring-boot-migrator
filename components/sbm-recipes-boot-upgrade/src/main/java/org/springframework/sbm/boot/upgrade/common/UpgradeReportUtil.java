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
package org.springframework.sbm.boot.upgrade.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.asciidoctor.*;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;

public class UpgradeReportUtil {

    public static String renderMarkdown(Map<String, Object> params, Configuration configuration) {
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("upgrade-asciidoc.ftl");
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String renderHtml(String markdown) {
        return renderHtml(markdown, "spring-html");
    }

    public static void writeHtml(String markdown, Path outputDir, String filename) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convert(markdown, Options.builder()
                    .mkDirs(true)
                    .toDir(outputDir.toFile())
                    .toFile(new File(filename))
                    .backend("spring-html")
                    .headerFooter(true)
                    .safe(SafeMode.UNSAFE)
                    .build()
        );
    }

    public static String renderHtml(String markdown, String backend) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        String html = asciidoctor.convert(markdown,
                                          Options.builder()
                                                  .toFile(true)
                                                  .backend(backend)
                                                  .headerFooter(true)
                                                  .safe(SafeMode.UNSAFE)
                                                  .build());
        return html;
    }
}
