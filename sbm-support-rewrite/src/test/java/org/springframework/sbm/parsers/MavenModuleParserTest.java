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

import org.apache.maven.model.Plugin;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.maven.BuildFileParser;
import org.springframework.sbm.parsers.maven.MavenModuleParser;
import org.springframework.sbm.parsers.maven.MavenProvenanceMarkerFactory;
import org.springframework.sbm.parsers.maven.MavenRuntimeInformation;
import org.springframework.sbm.test.util.DummyResource;

import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class MavenModuleParserTest {

    private MavenModuleParser sut;

    @BeforeEach
    void beforeEach() {
        sut = new MavenModuleParser(new ParserProperties(), new ModuleParser());
    }

    /**
     * Tests proving the behaviour of JavaParser in combination with JavaTypeCache
     */
    @Nested
    class JavaCompilerTypeResolutionTest {

        // Simple class A
        String aSource = """
                    package com.foo;
                    public class A{}
                    """;

        // Simple class B extending A
        String bSource = """
                    package com.bar;
                    import com.foo.A;
                    public class B extends A {}
                    """;
        private JavaParser.Builder<? extends JavaParser, ?> builder;

        @BeforeEach
        void beforeEach() {
            builder = JavaParser.fromJavaVersion();
        }

        // Parse A and B with one call to parse() on same instance
        // Same parser, same call, same type cache
        // -> Should resolve type A
        @Test
        @DisplayName("Same parser with shared type cache and one call should resolve types")
        void sameParserWithSharedTypeCacheAndOneCallShouldResolveTypes() {
            JavaParser javaParser = builder.build();

            List<SourceFile> parse = javaParser.parse(aSource, bSource).toList();

            String fullyQualifiedName = ((JavaType.FullyQualified) ((J.CompilationUnit) parse.get(1)).getClasses().get(0).getExtends().getType()).getFullyQualifiedName();
            assertThat(fullyQualifiedName).isEqualTo("com.foo.A");
        }

        // Parse A and B with separate calls to parse() on separate instances with separate type cache
        // Different parser, two calls, different type cache
        // -> Should NOT resolve type A
        @Test
        @DisplayName("Different parsers with separate caches and separate calls should not resolve types")
        void differentParsersWithSeparateCachesAndSeparateCallsShouldNotResolveTypes() {
            // parser 1 with dedicated type cache
            JavaTypeCache typeCache = new JavaTypeCache();
            builder.typeCache(typeCache);
            JavaParser javaParser = builder.build();
            SourceFile a = javaParser.parse(aSource).toList().get(0);

            // parser 2 with dedicated type cache
            JavaTypeCache typeCache2 = new JavaTypeCache();
            builder.typeCache(typeCache2);
            JavaParser javaParser2 = builder.build();
            SourceFile b = javaParser2.parse(bSource).toList().get(0);

            // Type A used in B not resolved
            String fullyQualifiedName2 = ((JavaType.FullyQualified) ((J.CompilationUnit) b).getClasses().get(0).getExtends().getType()).getFullyQualifiedName();
            String unknown = JavaType.Unknown.getInstance().getFullyQualifiedName();
            assertThat(fullyQualifiedName2).isEqualTo(unknown);
        }

        // Parse A and B with separate calls to parse() on separate instances with SHARED type cache
        // Different parser, two calls, same type cache
        // -> Should NOT resolve type A
        // -> Sharing the type cache is not enough!
        @Test
        @DisplayName("Different parsers with shared cache and separate calls should NOT resolve types")
        void differentParsersWithSharedCacheAndSeparateCallsShouldNotResolveTypes() {
            // Shared type cache
            JavaTypeCache typeCache = new JavaTypeCache();
            builder.typeCache(typeCache);

            // parser 1
            JavaParser javaParser = builder.build();
            // parser 2
            JavaParser javaParser2 = builder.build();

            SourceFile a = javaParser.parse(aSource).toList().get(0);
            SourceFile b = javaParser2.parse(bSource).toList().get(0);

            // Type A used in B IS resolved
            String fullyQualifiedName = ((JavaType.FullyQualified) ((J.CompilationUnit) b).getClasses().get(0).getExtends().getType()).getFullyQualifiedName();
            String unknown = JavaType.Unknown.getInstance().getFullyQualifiedName();
            assertThat(fullyQualifiedName).isEqualTo(unknown);
        }

        // Parse A and B with separate calls to parse() on SAME instances with SHARED type cache
        // Same parser, two calls, same type cache
        // -> Should resolve type A
        // -> Same parser, separate calls, same type cache
        // --> WORKS! That's what we need.
        @Test
        @DisplayName("Same parser with shared cache in separate calls should resolve types")
        void sameParserWithSharedCacheInSeparateCallsShouldResolveTypes() {
            // One shared type cache
            JavaTypeCache typeCache = new JavaTypeCache();
            builder.typeCache(typeCache);
            JavaParser javaParser = builder.build();

            // Same parser two calls
            SourceFile a = javaParser.parse(aSource).toList().get(0);
            SourceFile b = javaParser.parse(bSource).toList().get(0);

            J.CompilationUnit compilationUnitB = (J.CompilationUnit) b;
            String fullyQualifiedName2 = ((JavaType.FullyQualified) compilationUnitB.getClasses().get(0).getExtends().getType()).getFullyQualifiedName();
            // A can be resolved
            assertThat(fullyQualifiedName2).isEqualTo("com.foo.A");

            List<String> bTypesInUseFqNames = compilationUnitB.getTypesInUse().getTypesInUse().stream().map(fq -> ((JavaType.FullyQualified) fq).getFullyQualifiedName()).toList();
            // A is in typesInUse of B
            assertThat(bTypesInUseFqNames).contains("com.foo.A");
            //No markers were set
            assertThat(compilationUnitB.getMarkers().getMarkers()).isEmpty();
        }
    }


    @Test
    @DisplayName("parse simple project")
    void parseSimpleProject() {
        @Language("xml")
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.example</groupId>
                        <artifactId>example-1-parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>app</artifactId>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.junit.jupiter</groupId>
                            <artifactId>junit-jupiter-api</artifactId>
                            <version>5.9.3</version><scope>test</scope>
                            <scope>test</scope>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;
        @Language("java")
        String mainClass = """
                package com.example.app;
                import javax.validation.constraints.Min;
                                
                public class MainClass {
                    @Min("0")
                    private int number;
                }
                """;
        @Language("java")
        String testClass = """
                package com.example.app.test;
                import org.junit.jupiter.api.Test;
                import com.example.app.MainClass;
                                
                public class SomeTest {
                    @Test
                    void someTest() {
                        MainClass m = new MainClass();
                    }
                }
                """;

        Path baseDir = Path.of("./target").toAbsolutePath().normalize();

        Path pomXmlPath = baseDir.resolve("pom.xml").normalize();
        List<Resource> resources = List.of(
                new DummyResource(pomXmlPath, pomXml),
                new DummyResource(baseDir.resolve("src/main/java/com/example/app/MainClass.java"), mainClass),
                new DummyResource(baseDir.resolve("src/test/java/com/example/app/test/SomeTest.java"), testClass)
        );

        MavenRuntimeInformation mavenRuntimeInfo = new MavenRuntimeInformation();
        Plugin compilerPlugin = null;
        Properties properties = new Properties();
        String projectName = "app";
        String groupId = "com.example";
        String artifactId = "app";
        String version = "1.0.0-SNAPSHOT";

        List<Marker> provenanceMarkers = new MavenProvenanceMarkerFactory().generateProvenanceMarkers(
                baseDir,
                mavenRuntimeInfo,
                compilerPlugin,
                properties,
                projectName,
                groupId,
                artifactId,
                version
        );
        Map<Path, List<Marker>> provenanceMarkersMap = Map.of(pomXmlPath, provenanceMarkers);
        BuildFileParser buildFileParser = new BuildFileParser();
        List<Xml.Document> parsedBuildFiles = buildFileParser.parseBuildFiles(baseDir, List.of(resources.get(0)), List.of(), new RewriteExecutionContext(), false, provenanceMarkersMap);
        Xml.Document document = parsedBuildFiles.get(0);


        Collection<Path> classpath = new ArrayList<>();
        Collection<byte[]> classBytesClasspath = new ArrayList<>();

        ModuleParsingResult moduleParsingResult = sut.parseModule(baseDir, "", classpath, classBytesClasspath, resources);

        assertThat(moduleParsingResult.sourceFiles()).hasSize(2);
        assertThat(moduleParsingResult.sourceFiles().get(0).printAll()).isEqualTo(mainClass);
    }

    @Test
    @DisplayName("parse reactor project")
    void parseReactorProject() {
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
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
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
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
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

        Path baseDir = Path.of("./target").toAbsolutePath().normalize();
        List<Resource> resources = List.of(
                new DummyResource(baseDir.resolve("pom.xml"), parentPom),
                new DummyResource(baseDir.resolve("module-a/pom.xml"), moduleAPom),
                new DummyResource(baseDir.resolve("module-b/pom.xml"), moduleBPom),
                new DummyResource(baseDir.resolve("module-c/pom.xml"), moduleCPom)
        );
        ExecutionContext executionContext = new InMemoryExecutionContext();
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
        BuildFileParser buildFileParser = new BuildFileParser();
        List<Xml.Document> parsedBuildFiles = buildFileParser.parseBuildFiles(baseDir, resources, List.of(), executionContext, false, Map.of());

        Path locaMavenRepo = Path.of(System.getProperty("user.home")).resolve(".m2/repository");
        MavenArtifactDownloader artifactDownloader = new RewriteMavenArtifactDownloader(new LocalMavenArtifactCache(locaMavenRepo), null, t -> {throw new RuntimeException(t);});
        ClasspathExtractor classpathExtractor = new ClasspathExtractor(artifactDownloader);
        List<Path> classpath = classpathExtractor.extractClasspath(parsedBuildFiles.get(0).getMarkers().findFirst(MavenResolutionResult.class).get(), Scope.Compile);
        Collection<byte[]> classBytesClasspath = List.of();
        String modulePathSegment = "module-c";
        ModuleParsingResult result = sut.parseModule(baseDir, modulePathSegment, classpath, classBytesClasspath, resources);
        assertThat(result.sourceFiles()).hasSize(0);
    }

}
