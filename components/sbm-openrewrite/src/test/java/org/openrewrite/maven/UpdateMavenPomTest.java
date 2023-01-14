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
package org.openrewrite.maven;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

/**
 * @author Fabian Krüger
 */
public class UpdateMavenPomTest {
    final String parentPom = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!--
              ~ Copyright 2021 - 2022 the original author or authors.
              ~
              ~ Licensed under the Apache License, Version 2.0 (the "License");
              ~ you may not use this file except in compliance with the License.
              ~ You may obtain a copy of the License at
              ~
              ~      https://www.apache.org/licenses/LICENSE-2.0
              ~
              ~ Unless required by applicable law or agreed to in writing, software
              ~ distributed under the License is distributed on an "AS IS" BASIS,
              ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
              ~ See the License for the specific language governing permissions and
              ~ limitations under the License.
              -->
                        
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <groupId>org.example</groupId>
                <artifactId>parent</artifactId>
                <version>1.0-SNAPSHOT</version>
                <packaging>pom</packaging>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
                <modules>
                    <module>module1</module>
                    <module>module2</module>
                </modules>
                <dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-dependencies</artifactId>
                            <version>2.7.3</version>
                            <type>pom</type>
                            <scope>import</scope>
                        </dependency>
                    </dependencies>
                </dependencyManagement>
            </project>
            """;

    String module2 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!--
              ~ Copyright 2021 - 2022 the original author or authors.
              ~
              ~ Licensed under the Apache License, Version 2.0 (the "License");
              ~ you may not use this file except in compliance with the License.
              ~ You may obtain a copy of the License at
              ~
              ~      https://www.apache.org/licenses/LICENSE-2.0
              ~
              ~ Unless required by applicable law or agreed to in writing, software
              ~ distributed under the License is distributed on an "AS IS" BASIS,
              ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
              ~ See the License for the specific language governing permissions and
              ~ limitations under the License.
              -->
                        
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <artifactId>module2</artifactId>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
            </project>
            """;

    final String module1 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!--
              ~ Copyright 2021 - 2022 the original author or authors.
              ~
              ~ Licensed under the Apache License, Version 2.0 (the "License");
              ~ you may not use this file except in compliance with the License.
              ~ You may obtain a copy of the License at
              ~
              ~      https://www.apache.org/licenses/LICENSE-2.0
              ~
              ~ Unless required by applicable law or agreed to in writing, software
              ~ distributed under the License is distributed on an "AS IS" BASIS,
              ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
              ~ See the License for the specific language governing permissions and
              ~ limitations under the License.
              -->
                        
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <artifactId>module1</artifactId>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                    <spring-boot.version>2.7.2</spring-boot.version>
                </properties>
                <dependencies>
                    <dependency>
                        <groupId>org.example</groupId>
                        <artifactId>module2</artifactId>
                        <version>${project.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.junit.jupiter</groupId>
                        <artifactId>junit-jupiter</artifactId>
                        <version>5.8.2</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """;

    @Test
    void test_renameMe() {
        List<Xml.Document> documentList = MavenParser.builder().build().parse(parentPom, module2, module1);
    }



}
