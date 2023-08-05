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
package org.springframework.sbm.recipes;

import org.springframework.sbm.IntegrationTestBaseClass;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Boyko
 */
@Disabled("See https://github.com/pivotal/spring-boot-migrator/issues/471")
public class Spring1xTo2xRecipeIntegrationTest extends IntegrationTestBaseClass {

    private static final String SPRING_1X_TO_2X_RECIPE = "upgrade-boot-1x-to-2x";

    @Override
    protected String getTestSubDir() {
        return "spring-petclinic-1x";
    }

    @Test
    @Tag("integration")
    void happyPath() {

        String expectedPomSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "  xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.springframework.samples</groupId>\n" +
                "  <artifactId>spring-petclinic</artifactId>\n" +
                "  <version>1.5.1</version>\n" +
                "\n" +
                "  <parent>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "    <version>2.4.4</version>\n" +
                "  </parent>\n" +
                "  <name>petclinic</name>\n" +
                "\n" +
                "  <properties>\n" +
                "\n" +
                "    <!-- Generic properties -->\n" +
                "    <java.version>1.8</java.version>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\n" +
                "\n" +
                "    <!-- Web dependencies -->\n" +
                "    <webjars-bootstrap.version>3.3.6</webjars-bootstrap.version>\n" +
                "    <webjars-jquery-ui.version>1.11.4</webjars-jquery-ui.version>\n" +
                "    <webjars-jquery.version>2.2.4</webjars-jquery.version>\n" +
                "    <wro4j.version>1.8.0</wro4j.version>\n" +
                "\n" +
                "    <cobertura.version>2.7</cobertura.version>\n" +
                "\n" +
                "  </properties>\n" +
                "\n" +
                "  <dependencies>\n" +
                "    <!-- Spring and Spring Boot dependencies -->\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-actuator</artifactId>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-cache</artifactId>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-web</artifactId>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-thymeleaf</artifactId>\n" +
                "      <exclusions>\n" +
                "      \t<exclusion>\n" +
                "      \t\t<groupId>nz.net.ultraq.thymeleaf</groupId>\n" +
                "      \t\t<artifactId>thymeleaf-layout-dialect</artifactId>\n" +
                "      \t</exclusion>\n" +
                "      </exclusions>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-validation</artifactId>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-starter-test</artifactId>\n" +
                "      <scope>test</scope>\n" +
                "      <version>2.4.4</version>\n" +
                "      <exclusions>\n" +
                "        <exclusion>\n" +
                "          <groupId>junit</groupId>\n" +
                "          <artifactId>junit</artifactId>\n" +
                "        </exclusion>\n" +
                "      </exclusions>\n" +
                "    </dependency>\n" +
                "\n" +
                "    <!-- Databases - Uses HSQL by default -->\n" +
                "    <dependency>\n" +
                "      <groupId>org.hsqldb</groupId>\n" +
                "      <artifactId>hsqldb</artifactId>\n" +
                "      <scope>runtime</scope>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>mysql</groupId>\n" +
                "      <artifactId>mysql-connector-java</artifactId>\n" +
                "      <scope>runtime</scope>\n" +
                "    </dependency>\n" +
                "\n" +
                "    <!-- caching -->\n" +
                "    <dependency>\n" +
                "      <groupId>javax.cache</groupId>\n" +
                "      <artifactId>cache-api</artifactId>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>javax.validation</groupId>\n" +
                "      <artifactId>validation-api</artifactId>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.ehcache</groupId>\n" +
                "      <artifactId>ehcache</artifactId>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.webjars</groupId>\n" +
                "      <artifactId>jquery</artifactId>\n" +
                "      <version>${webjars-jquery.version}</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.webjars</groupId>\n" +
                "      <artifactId>jquery-ui</artifactId>\n" +
                "      <version>${webjars-jquery-ui.version}</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>org.webjars</groupId>\n" +
                "      <artifactId>bootstrap</artifactId>\n" +
                "      <version>${webjars-bootstrap.version}</version>\n" +
                "    </dependency>\n" +
                "    <!-- end of webjars -->\n" +
                "\n" +
                "    <dependency>\n" +
                "      <groupId>org.springframework.boot</groupId>\n" +
                "      <artifactId>spring-boot-devtools</artifactId>\n" +
                "      <scope>runtime</scope>\n" +
                "      <version>2.4.4</version>\n" +
                "    </dependency>\n" +
//                "    <dependency>\n" +
//                "      <groupId>org.junit.jupiter</groupId>\n" +
//                "      <artifactId>junit-jupiter-api</artifactId>\n" +
//                "      <version>5.7.1</version>\n" +
//                "      <scope>test</scope>\n" +
//                "    </dependency>\n" +
//                "    <dependency>\n" +
//                "      <groupId>org.junit.jupiter</groupId>\n" +
//                "      <artifactId>junit-jupiter-engine</artifactId>\n" +
//                "      <version>5.7.1</version>\n" +
//                "      <scope>test</scope>\n" +
//                "    </dependency>\n" +
                "  </dependencies>\n" +
                "\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "      <plugin>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <!-- Spring Boot Actuator displays build-related information\n" +
                "              if a META-INF/build-info.properties file is present -->\n" +
                "            <goals>\n" +
                "              <goal>build-info</goal>\n" +
                "            </goals>\n" +
                "            <configuration>\n" +
                "              <additionalProperties>\n" +
                "                <encoding.source>${project.build.sourceEncoding}</encoding.source>\n" +
                "                <encoding.reporting>${project.reporting.outputEncoding}</encoding.reporting>\n" +
                "                <java.source>${maven.compiler.source}</java.source>\n" +
                "                <java.target>${maven.compiler.target}</java.target>\n" +
                "              </additionalProperties>\n" +
                "            </configuration>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "\n" +
                "      <!-- Spring Boot Actuator displays build-related information if a git.properties\n" +
                "        file is present at the classpath -->\n" +
                "      <plugin>\n" +
                "        <groupId>pl.project13.maven</groupId>\n" +
                "        <artifactId>git-commit-id-plugin</artifactId>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>revision</goal>\n" +
                "            </goals>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "        <configuration>\n" +
                "          <verbose>true</verbose>\n" +
                "          <dateFormat>yyyy-MM-dd'T'HH:mm:ssZ</dateFormat>\n" +
                "          <generateGitPropertiesFile>true</generateGitPropertiesFile>\n" +
                "          <generateGitPropertiesFilename>${project.build.outputDirectory}/git.properties\n" +
                "          </generateGitPropertiesFilename>\n" +
                "          <failOnNoGitDirectory>false</failOnNoGitDirectory>\n" +
                "        </configuration>\n" +
                "      </plugin>\n" +
                "\n" +
                "      <plugin>\n" +
                "        <groupId>ro.isdc.wro4j</groupId>\n" +
                "        <artifactId>wro4j-maven-plugin</artifactId>\n" +
                "        <version>${wro4j.version}</version>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <phase>generate-resources</phase>\n" +
                "            <goals>\n" +
                "              <goal>run</goal>\n" +
                "            </goals>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "        <configuration>\n" +
                "          <wroManagerFactory>ro.isdc.wro.maven.plugin.manager.factory.ConfigurableWroManagerFactory</wroManagerFactory>\n" +
                "          <cssDestinationFolder>${project.build.directory}/classes/static/resources/css</cssDestinationFolder>\n" +
                "          <wroFile>${basedir}/src/main/wro/wro.xml</wroFile>\n" +
                "          <extraConfigFile>${basedir}/src/main/wro/wro.properties</extraConfigFile>\n" +
                "          <contextFolder>${basedir}/src/main/less</contextFolder>\n" +
                "        </configuration>\n" +
                "        <dependencies>\n" +
                "          <dependency>\n" +
                "            <groupId>org.mockito</groupId>\n" +
                "            <artifactId>mockito-core</artifactId>\n" +
                "            <version>${mockito.version}</version>\n" +
                "          </dependency>\n" +
                "          <dependency>\n" +
                "            <groupId>org.webjars</groupId>\n" +
                "            <artifactId>bootstrap</artifactId>\n" +
                "            <version>${webjars-bootstrap.version}</version>\n" +
                "          </dependency>\n" +
                "        </dependencies>\n" +
                "      </plugin>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "  <reporting>\n" +
                "    <plugins>\n" +
                "    </plugins>\n" +
                "  </reporting>\n" +
                "\n" +
                "  <!-- Apache 2 license -->\n" +
                "  <licenses>\n" +
                "    <license>\n" +
                "      <name>Apache License, Version 2.0</name>\n" +
                "      <url>http://www.apache.org/licenses/LICENSE-2.0</url>\n" +
                "    </license>\n" +
                "  </licenses>\n" +
                "\n" +
                "</project>\n";

        intializeTestProject();
        scanProject();

        assertRecipeApplicable(SPRING_1X_TO_2X_RECIPE);
        applyRecipe(SPRING_1X_TO_2X_RECIPE);

        String pomSource = super.loadFile(Path.of("pom.xml"));
        assertThat(pomSource)
                .as(TestDiff.of(pomSource, expectedPomSource))
                .isEqualTo(expectedPomSource);

        scanProject();
        assertRecipeNotApplicable(SPRING_1X_TO_2X_RECIPE);
    }

}
