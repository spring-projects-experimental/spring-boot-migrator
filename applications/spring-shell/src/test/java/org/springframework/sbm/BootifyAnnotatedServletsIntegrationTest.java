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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BootifyAnnotatedServletsIntegrationTest extends IntegrationTestBaseClass {


    @Override
    protected String getTestSubDir() {
        return "bootify-servlets";
    }

    @Tag("integration")
    @Test
    void happyPath() {

        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "         xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>1.8</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.ejb</groupId>\n" +
                "            <artifactId>javax.ejb-api</artifactId>\n" +
                "            <version>3.2</version>\n" +
                "            <scope>provided</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.enterprise</groupId>\n" +
                "            <artifactId>cdi-api</artifactId>\n" +
                "            <version>1.2</version>\n" +
                "            <scope>provided</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.servlet</groupId>\n" +
                "            <artifactId>javax.servlet-api</artifactId>\n" +
                "            <version>4.0.1</version>\n" +
                "        </dependency>\n" +
                "   </dependencies>\n" +
                "</project>\n";

        String servletClass =
                "package org.jboss.as.quickstarts.helloworld;\n" +
                        "\n" +
                        "import java.io.IOException;\n" +
                        "import java.io.PrintWriter;\n" +
                        "\n" +
                        "import javax.servlet.ServletException;\n" +
                        "import javax.servlet.annotation.WebServlet;\n" +
                        "import javax.servlet.http.HttpServlet;\n" +
                        "import javax.servlet.http.HttpServletRequest;\n" +
                        "import javax.servlet.http.HttpServletResponse;\n" +
                        "\n" +
                        "@WebServlet(\"/HelloWorld\")\n" +
                        "public class HelloWorldServlet extends HttpServlet {\n" +
                        "    @Override\n" +
                        "    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {\n" +
                        "        resp.getWriter().append(\"Hello World!\");\n" +
                        "    }\n" +
                        "}\n";

        writeFile(pomXml, "pom.xml");
        writeJavaFile(servletClass);

        executeMavenGoals(getTestDir(), "compile");

        scanProject();

        assertApplicableRecipesContain(
                "initialize-spring-boot-migration",
                "migrate-annotated-servlets"
        );

        applyRecipe("initialize-spring-boot-migration");
        applyRecipe("migrate-annotated-servlets");

        String content = loadJavaFile("org.jboss.as.quickstarts.helloworld", "SpringBootApp");
        assertThat(content).contains("@SpringBootApplication").withFailMessage(() -> "@SpringBootApplication annotation not found");
        assertThat(content).contains("@ServletComponentScan").withFailMessage(() -> "@ServletComponentScan annotation not found");

        executeMavenGoals(getTestDir(), "package");
//        int port = springBootStart();

        executeMavenGoals(getTestDir(), "package", "spring-boot:build-image");

        Integer port = startDockerContainer("jboss-sample:1.0.0", "/HelloWorld", 8080);

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String response = testRestTemplate.getForObject("http://localhost:" + port + "/HelloWorld", String.class);

        assertThat(response).isEqualTo("Hello World!");

//        springBootStop();
    }

}
