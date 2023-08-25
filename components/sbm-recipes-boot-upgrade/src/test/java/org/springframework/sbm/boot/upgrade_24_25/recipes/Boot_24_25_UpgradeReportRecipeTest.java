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
package org.springframework.sbm.boot.upgrade_24_25.recipes;

import org.junit.jupiter.api.Disabled;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_24_25_UpgradeReportRecipeTest {

    @Test
    @Tag("integration")
    @Disabled("FIXME: When running in IntelliJ it succeeds but in Maven the <DOCTYPE> tyg is not generated?!")
    void generateReportTest() throws IOException {
        // copy 'testcode/spring-boot-2.4-to-2.5-example/given' to target and run recipe 'boot-2.4-2.5-upgrade-report' against it
        String applicationDir = "spring-boot-2.4-to-2.5-example";
        Path from = Path.of("./testcode").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("boot-2.4-2.5-upgrade-report");

        // get expected and created report with replaced dynamic parts (paths and timestamps)
        Path resultDir = RecipeIntegrationTestSupport.getResultDir(applicationDir);
        String generatedReportContent = replaceDynamicPartsInReport(resultDir.resolve("Upgrade-Spring-Boot-2.4-to-2.5.html"), resultDir);

        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        String revision = gitSupport.getLatestCommit(resultDir.toAbsolutePath().toFile()).get().getHash();

        String expectedReport =
                getContent(new ClassPathResource("/expected-report.html").getFile().toPath())
                        .replace("{{REVISION_NUMBER}}", revision);

        // verify generated result matches expected report
        assertThat(generatedReportContent).isEqualTo(expectedReport);
    }

    String replaceDynamicPartsInReport(Path report, Path resultDir) throws IOException {
        String content = getContent(report);

        return content.replaceAll(resultDir.toAbsolutePath().normalize().toString(), "{{PATH_TO_PROJECT}}")
                .replaceAll("<p class=\"tableblock\">[0-9]{1,2} [a-zA-Z]{3} [0-9]{4}, [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}</p>", "<p class=\"tableblock\">{{CREATION_DATE}}</p>")
                .replaceAll("Last updated [0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2} \\+[0-9]{4}", "Last updated {{LAST_UPDATED}}");
    }

    @NotNull
    private String getContent(Path report) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        return new String(Files.readAllBytes(report), charset).replaceAll("\n$", "");
    }
}
