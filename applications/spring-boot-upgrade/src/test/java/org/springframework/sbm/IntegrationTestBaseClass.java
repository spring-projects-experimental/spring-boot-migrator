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
package org.springframework.sbm;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.nio.file.Path;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false",
        "sbm.gitSupportEnabled=false"
})
public abstract class IntegrationTestBaseClass {

    /**
     * Points to the source root directory where example projects are expected.
     *
     * See {@link #getTestSubDir()}.
     */
    public static final String TESTCODE_DIR = "src/test/resources/testcode/";

    /**
     * Points to the target root directory where example projects will be copied to.
     *
     * See {@link #getTestSubDir()}.
     */
    public static final String INTEGRATION_TEST_DIR = "./target/sbm-integration-test/";

    private Path testDir;

    private String output;


    @BeforeEach
    public void beforeEach() throws IOException {
        testDir = Path.of(INTEGRATION_TEST_DIR).resolve(getTestSubDir());
        clearTestDir();
        testDir.resolve(this.getClass().getName());
        FileUtils.forceMkdir(testDir.toFile());
        testDir = testDir.toRealPath();
    }

    /**
     * Copies example project used for integrationTest.
     */
    protected void intializeTestProject() {
        try {
            Path s = Path.of(TESTCODE_DIR).resolve(getTestSubDir());
            FileUtils.copyDirectory(s.toFile(), getTestDir().toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected abstract String getTestSubDir();

    protected Path getTestDir() {
        return testDir;
    }


    @AfterEach
    public void afterEach() throws IOException {
        clearTestDir();
    }

    private void clearTestDir() throws IOException {
        if (getTestDir().toFile().exists()) {
            FileUtils.forceDelete(getTestDir().toFile());
        }
    }
}
