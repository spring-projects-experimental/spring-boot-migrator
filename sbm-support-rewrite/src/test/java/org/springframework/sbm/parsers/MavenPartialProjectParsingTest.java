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
package org.springframework.sbm.parsers;

import org.checkerframework.checker.units.qual.A;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.ExecutionContext;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.AddDependencyVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.sbm.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.ProjectResourceSetFactory;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.test.util.DummyResource;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = SbmSupportRewriteConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MavenPartialProjectParsingTest {

    @Autowired
    RewriteProjectParser rewriteProjectParser;

    @Autowired
    ProjectResourceSetFactory resourceSetFactory;

    @Autowired
    private ExecutionContext executionContext;

    @TempDir Path baseDir;

    private static RewriteProjectParsingResult parsingResult;

    @Language("xml")
    String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>example-1-parent</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <properties>
                         <maven.compiler.target>17</maven.compiler.target>
                         <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <modules>
                        <module>module-a</module>
                        <module>module-b</module>
                        <module>module-c</module>
                    </modules>
                </project>
                """;

    @Language("xml")
    String moduleAPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.example</groupId>
                        <artifactId>example-1-parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-a</artifactId>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>com.example</groupId>
                            <artifactId>module-b</artifactId>
                            <version>${project.version}</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

    @Language("xml")
    String moduleBPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.example</groupId>
                        <artifactId>example-1-parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-b</artifactId>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>com.example</groupId>
                            <artifactId>module-c</artifactId>
                            <version>${project.version}</version>
                        </dependency>
                    </dependencies>                  
                </project>
                """;

    @Language("xml")
    String moduleCPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.example</groupId>
                        <artifactId>example-1-parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-c</artifactId>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                </project>
                """;

    @Test
    @Order(1)
    @DisplayName("parse reactor project")
    void parseReactorProject() {
        List<Resource> resources = List.of(
                new DummyResource(baseDir.resolve("pom.xml"), parentPom),
                new DummyResource(baseDir.resolve("module-a/pom.xml"), moduleAPom),
                new DummyResource(baseDir.resolve("module-b/pom.xml"), moduleBPom),
                new DummyResource(baseDir.resolve("module-b/src/main/java/MainB.java"), """
                        public class MainB {
                            String name;
                        }
                        """),
                new DummyResource(baseDir.resolve("module-c/pom.xml"), moduleCPom)
        );


        parsingResult = rewriteProjectParser.parse(baseDir, resources);
        // TODO: verify initial parsing result here
    }

    @Test
    @Order(2)
    @DisplayName("adding dependency to B")
    void addingDependencyToB() {
        GenericOpenRewriteRecipe<AddDependencyVisitor> recipe = new GenericOpenRewriteRecipe<>(() -> new AddDependencyVisitor("com.example", "module-b", "0.1.0-SNAPSHOT", null, null, null, null, null, null, null));
        ProjectResourceSet resourceSet = resourceSetFactory.create(baseDir, parsingResult.sourceFiles());
        resourceSet.apply(recipe);
        // TODO: assertion the classpath change
    }
    
    @Test
    @Order(3)
    @DisplayName("add code using the new types")
    void addCodeUsingTheNewTypes() {
        JavaIsoVisitor<ExecutionContext> visitor = new JavaIsoVisitor<>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
//                cd.getBody().getStatements().get(0);
//                ((J.VariableDeclarations)cd.getBody().getStatements().get(0))
                JavaTemplate.builder("@Nullable").javaParser()
                return cd;
            }
        };

        GenericOpenRewriteRecipe<JavaIsoVisitor<ExecutionContext>> recipe = new GenericOpenRewriteRecipe<>(() -> visitor);
        ProjectResourceSet resourceSet = resourceSetFactory.create(baseDir, parsingResult.sourceFiles());
        resourceSet.apply(recipe);
    }




/*
        // used to calculate paths to other modules.
        // TODO: Remove and filter all provided resources by module path segment
        MavenProject mavenProject = null;
        // used to retrieve sourcePath which can be calculated having the module path segment and baseDir
        Xml.Document moduleBuildFile = null;
        // required, can be taken from a resource in same source dir?!
        List<Marker> provenanceMarkers = null;
        // Make this a Spring bean
        List<NamedStyles> styles = null;
        // provided as bean
        ExecutionContext executionContext = null;
        // required
        Path baseDir = null;
        sut.parseModuleSourceFiles(resources, mavenProject, moduleBuildFile, provenanceMarkers, styles, executionContext, baseDir);
*/
//        BuildFileParser buildFileParser = new BuildFileParser();
//        List<Xml.Document> parsedBuildFiles = buildFileParser.parseBuildFiles(baseDir, resources, List.of(), executionContext, false, Map.of());
//
//        Path locaMavenRepo = Path.of(System.getProperty("user.home")).resolve(".m2/repository");
//        MavenArtifactDownloader artifactDownloader = new RewriteMavenArtifactDownloader(new LocalMavenArtifactCache(locaMavenRepo), null, t -> {throw new RuntimeException(t);});
//        ClasspathExtractor classpathExtractor = new ClasspathExtractor(artifactDownloader);
//        List<Path> classpath = classpathExtractor.extractClasspath(parsedBuildFiles.get(0).getMarkers().findFirst(MavenResolutionResult.class).get(), Scope.Compile);
//        Collection<byte[]> classBytesClasspath = List.of();
//        String modulePathSegment = "module-c";
//        ModuleParsingResult result = sut.parseModule(baseDir, modulePathSegment, classpath, classBytesClasspath, resources);
//        assertThat(result.sourceFiles()).hasSize(0);
//    }

}
