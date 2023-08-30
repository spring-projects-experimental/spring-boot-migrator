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
package org.springframework.sbm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled("Temporary disabled before CI will be fixed with docker in docker issue: #351")
public class MigrateAnnotatedServletsIntegrationTest extends IntegrationTestBaseClass {


    @Override
    protected String getTestSubDir() {
        return "bootify-servlets";
    }

    @Tag("integration")
    @Test
    void happyPath() {

        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
                         xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.vmware.example</groupId>
                    <artifactId>jboss-sample</artifactId>
                    <version>1.0.0</version>
                    <properties>
                        <maven.compiler.source>1.8</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>javax.ejb</groupId>
                            <artifactId>javax.ejb-api</artifactId>
                            <version>3.2</version>
                            <scope>provided</scope>
                        </dependency>
                        <dependency>
                            <groupId>javax.enterprise</groupId>
                            <artifactId>cdi-api</artifactId>
                            <version>1.2</version>
                            <scope>provided</scope>
                        </dependency>
                        <dependency>
                            <groupId>javax.servlet</groupId>
                            <artifactId>javax.servlet-api</artifactId>
                            <version>4.0.1</version>
                        </dependency>
                   </dependencies>
                </project>
                """;

        String servletClass = """
                package org.jboss.as.quickstarts.helloworld;
                                
                import java.io.IOException;
                import java.io.PrintWriter;
                                
                import javax.servlet.ServletException;
                import javax.servlet.annotation.WebServlet;
                import javax.servlet.http.HttpServlet;
                import javax.servlet.http.HttpServletRequest;
                import javax.servlet.http.HttpServletResponse;
                                
                @WebServlet("/HelloWorld")
                public class HelloWorldServlet extends HttpServlet {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        resp.getWriter().append("Hello World!");
                    }
                }
                """;

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

        String servlet = loadJavaFile("org.jboss.as.quickstarts.helloworld", "HelloWorldServlet");
        assertThat(content).contains("@SpringBootApplication").withFailMessage(() -> "@SpringBootApplication annotation not found");

        executeMavenGoals(getTestDir(), "spring-boot:build-image");

        Integer port = startDockerContainer("jboss-sample:1.0.0", "/HelloWorld", 8080);

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String response = testRestTemplate.getForObject("http://localhost:" + port + "/HelloWorld", String.class);

        assertThat(response).isEqualTo("Hello World!");

    }

    @Test
    void recipeNBotApplicableWhenOnlyFilterExists() {
        String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
                         xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.vmware.example</groupId>
                    <artifactId>jboss-sample</artifactId>
                    <version>1.0.0</version>
                    <properties>
                        <maven.compiler.source>1.8</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>javax.servlet</groupId>
                            <artifactId>javax.servlet-api</artifactId>
                            <version>4.0.1</version>
                        </dependency>
                   </dependencies>
                </project>
                """;

        String servletFilterClass = """
                package org.jboss.as.quickstarts.helloworld;
                import javax.servlet.annotation.WebFilter;
                
                @WebFilter("/")
                public class MyFilter {
                }
                """;

        writeFile(pom, "pom.xml");
        writeJavaFile(servletFilterClass);

        executeMavenGoals(getTestDir(), "compile");

        scanProject();

        assertRecipeNotApplicable(
                "migrate-annotated-servlets"
        );
    }
}
