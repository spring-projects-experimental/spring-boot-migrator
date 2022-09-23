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

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateSimpleMuleAppDataweaveIntegrationTest extends IntegrationTestBaseClass {
    private final RestTemplate restTemplate = new RestTemplate();
    private static RunningNetworkedContainer tmDataweaveContainer;

    @Override
    protected String getTestSubDir() {

        return "mule-app/spring-dw-mule";

    }

    @BeforeAll
    public static void beforeAll() {
        IntegrationTestBaseClass.beforeAll();

        // Will need to ensure this is set globally for the test
        System.setProperty("sbm.muleTriggerMeshTransformEnabled", "true");

        // start TriggerMesh Dataweave Translator
        HashMap<String, String> envMap = new HashMap<>();
        envMap.put("NAMESPACE", "default");
        envMap.put("DATAWEAVETRANSFORMATION_ALLOW_SPELL_OVERRIDE", "true");

        tmDataweaveContainer = startDockerContainer(
                new NetworkedContainer(
                        "gcr.io/triggermesh/dataweavetransformation-adapter:v1.21.0",
                        List.of(8080),
                        "dwhost"),
                null,
                envMap);
        if (!tmDataweaveContainer.getContainer().isRunning()) {
            throw new RuntimeException("TriggerMesh Dataweave Transformer container could not be started");
        }
    }

    @AfterAll
    public static void afterAll() {
        if (tmDataweaveContainer != null && tmDataweaveContainer.getContainer() != null) {
            tmDataweaveContainer.getContainer().stop();
        }
    }

    @Test
    @Tag("integration")
    public void  t2_dataweaveIntegrationWorks() {
        intializeTestProject();
        scanProject();
        applyRecipe("initialize-spring-boot-migration");
        applyRecipe("migrate-mule-to-triggermesh-boot");

        executeMavenGoals(getTestDir(), "clean", "package", "spring-boot:build-image");

        int dwPort = tmDataweaveContainer.getContainer().getMappedPort(8080);
        HashMap<String, String> runtimeEnv = new HashMap<>();
        runtimeEnv.put("K_SINK", "http://dwhost:8080");

        RunningNetworkedContainer container = startDockerContainer(
                new NetworkedContainer("hellomuledw-migrated:1.0-SNAPSHOT", List.of(9081), "spring"),
                tmDataweaveContainer.getNetwork(),
                runtimeEnv);

        checkSendHttpMessage(container.getContainer().getMappedPort(9081));
        checkTranslatedInboundGatewayHttpMessage(container.getContainer().getMappedPort(9081));
    }

    private void checkTranslatedInboundGatewayHttpMessage(int port) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/dwtest", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("\n{\n  \"greeting\": \"hello from SBM\"\n}");
    }

    private void checkSendHttpMessage(int port) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/test", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
