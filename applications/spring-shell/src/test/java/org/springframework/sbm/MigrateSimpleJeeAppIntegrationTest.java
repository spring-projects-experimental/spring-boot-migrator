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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriUtils;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled("Temporary disabled before CI will be fixed with docker in docker issue: #351")
public class MigrateSimpleJeeAppIntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "jee-app";
    }

    @Test
    @Tag("integration")
    void migrateSimpleJeeApp() {
        intializeTestProject();

        scanProject();

        applyRecipe("initialize-spring-boot-migration");
        applyRecipe("migrate-jax-rs");
        applyRecipe("migrate-ejb-jar-deployment-descriptor");
        applyRecipe("migrate-stateless-ejb");
        applyRecipe("migrate-annotated-servlets");
        applyRecipe("migrate-jpa-to-spring-boot");

        // simulate manual step
        replaceFile(
                getTestDir().resolve("src/test/java/com/example/jee/app/PersonServiceTest.java"),
                getTestDir().resolve("manual-step/BootifiedPersonServiceTest.java")
        );

        String localBusinessInterface = loadJavaFile("com.example.jee.app.ejb.local", "ABusinessInterface");
        // verify @Local was removed
        assertThat(localBusinessInterface).doesNotContain("@Local");
        // verify import for @Local was removed
        assertThat(localBusinessInterface).doesNotContain("import javax.ejb.Local;");


        String localBean = loadJavaFile("com.example.jee.app.ejb.local", "ABean");
        // verify @Stateless was removed
        assertThat(localBean).doesNotContain("@Stateless");
        // verify import for @Stateless was removed
        assertThat(localBean).doesNotContain("import javax.ejb.Stateless;");
        // verify @Service was added
        assertThat(localBean).contains("@Service");
        // verify import for @Stateless was added
        assertThat(localBean).contains("import org.springframework.stereotype.Service");

        // verify
        String noInterfaceViewBean = loadJavaFile("com.example.jee.app.ejb.local", "NoInterfaceViewBean");
        assertThat(noInterfaceViewBean).isEqualTo(
                "package com.example.jee.app.ejb.local;\n" +
                "\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "@Service(\"noInterfaceView\")\n" +
                "@Transactional\n" +
                "public class NoInterfaceViewBean {\n" +
                "\n" +
                "}");

        File ejbJarXml = new File("./target/sbm-integration-test/src/main/resources/META-INF/ejb-jar.xml");
        assertThat(ejbJarXml).doesNotExist();

        executeMavenGoals(getTestDir(), "clean", "package", "spring-boot:build-image");
        Integer port = startDockerContainer("jee-app:8.0.5-SNAPSHOT", "/HelloWorld", 8080);

        TestRestTemplate testRestTemplate = new TestRestTemplate();

        // Test Servlet
        String response = testRestTemplate.getForObject("http://localhost:" + port + "/HelloWorld", String.class);
        assertThat(response).isEqualTo("Hello World!");

        // Test EJB, RestService and JPA
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        String path = "http://localhost:" + port + "/json/Bob";
        String url = UriUtils.encodePath(path, "UTF-8");
        String restResponse = testRestTemplate.postForObject(url, httpEntity, String.class);
        assertThat(restResponse).isEqualTo("1:Bob");

        String path2 = "http://localhost:" + port + "/json/Sigourney";
        String url2 = UriUtils.encodePath(path2, "UTF-8");
        String restResponse2 = testRestTemplate.postForObject(url2, httpEntity, String.class);
        assertThat(restResponse2).isEqualTo("2:Sigourney");

        String response3 = testRestTemplate.exchange("http://localhost:" + port + "/json",
                HttpMethod.GET,
                httpEntity,
                String.class).getBody();
        assertThat(response3).isEqualTo("Bob, Sigourney");

        // test ABean EJB migration through Rest Controller
        String theA = testRestTemplate.getForObject("http://localhost:" + port + "/a", String.class);
        assertThat(theA).isEqualTo("A");

//        executeMavenGoals(getTestDir(), "clean", "package", "spring-boot:stop", "-Dspring-boot.stop.fork=true", "-Dspring-boot.start.jmxPort="+jmxPort);
    }

}
