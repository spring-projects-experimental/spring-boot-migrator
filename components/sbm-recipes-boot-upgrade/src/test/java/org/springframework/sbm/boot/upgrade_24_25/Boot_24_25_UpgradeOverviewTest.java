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
package org.springframework.sbm.boot.upgrade_24_25;

import org.junit.jupiter.api.Test;

class Boot_24_25_UpgradeOverviewTest {
    @Test
    void testIsApplicable_shouldReturnTrue_withAnySpringBootDependency25() {
        String pomXml =
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                    "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "    <parent>\n" +
                    "        <artifactId>spring-boot-migrator</artifactId>\n" +
                    "        <groupId>org.springframework.sbm</groupId>\n" +
                    "        <version>0.9.2-SNAPSHOT</version>\n" +
                    "        <relativePath>../../pom.xml</relativePath>\n" +
                    "    </parent>\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <artifactId>spring-boot-upgrade</artifactId>\n" +
                    "    <properties>\n" +
                    "        <maven.compiler.source>11</maven.compiler.source>\n" +
                    "        <maven.compiler.target>11</maven.compiler.target>\n" +
                    "    </properties>\n" +
                    "    <dependencies>\n" +
                    "       <dependency>\n" +
                    "           <groupId>org.springframework</groupId>\n" +
                    "           <artifactId>spring-boot-starter</artifactId>\n" +
                    "           <version>2.4.12</version>" +
                    "       </dependency>\n" +
                    "    </dependencies>\n" +
                    "</project>";

        // FIXME: add test when upgrade branch is merged back...
    }
}
