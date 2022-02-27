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
package org.springframework.sbm.build.migration.visitor;

import org.springframework.sbm.openrewrite.MavenRefactoringTestHelper;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Boyko
 */
public class SetPackagingTest {

    @Test
    public void swapExistingPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>war</packaging>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging("jar");

        MavenRefactoringTestHelper.verifyChange(before, expected, r);
    }

    @Test
    public void removeExistingPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>war</packaging>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging(null);

        MavenRefactoringTestHelper.verifyChange(before, expected, r);
    }

    @Test
    public void leaveExistingPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>war<!-- hoho --></packaging>\n" +
                        "\n" +
                        "</project>";


        SetPackaging r = new SetPackaging("war");

        MavenRefactoringTestHelper.verifyNoChange(before, r);
    }

    @Test
    public void addNonExistingPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>war</packaging>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging("jar");

        MavenRefactoringTestHelper.verifyChange(before, expected, r);
    }

    @Test
    public void removeNonExistingPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging(null);

        MavenRefactoringTestHelper.verifyNoChange(before, r);
    }

    // No packaging is JAR packaging, but adding it via recipe would still add the tag with jar value
    @Test
    public void addDefaultJarPackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging("jar");

        MavenRefactoringTestHelper.verifyChange(before, expected, r);
    }

    @Test
    public void ignoreCasePackaging() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "\n" +
                        "</project>";

        SetPackaging r = new SetPackaging("Jar");

        MavenRefactoringTestHelper.verifyNoChange(before, r);
    }


}
