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
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
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
}
