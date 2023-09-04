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

import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.openrewrite.maven.cache.CompositeMavenPomCache;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.maven.cache.RocksdbMavenPomCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MavenPomCacheTest {

    private static final String originalArchDataModel = System.getProperty("sun.arch.data.model");

    @Nested
    @SetSystemProperty(key="sun.arch.data.model", value = "64")
    class GivenA64BitSystem {

        @Nested
        @SpringBootTest(properties = {"parser.pomCacheEnabled=true", "parser.pomCacheDirectory=target"})
        @DirtiesContext
        class WhenPomCacheIsEnabledIsTrue {

            @Autowired
            private MavenPomCache mavenPomCache;

            @Test
            @DisplayName("When pomCacheEnabled is true a CompositeMavenPomCache gets be used")
            void compositePomCacheShouldBeUsed() {
                assertThat(mavenPomCache).isInstanceOf(CompositeMavenPomCache.class);
            }

            @Test
            @DisplayName("The used CompositeMavenPomCache should be Rocksdb and InMemory cache")
            void compositePomCacheShouldBeUsed2() {
                assertThat(mavenPomCache).isInstanceOf(CompositeMavenPomCache.class);
                assertThat(
                    List.of(
                        ReflectionTestUtils.getField(mavenPomCache, "l1").getClass(),
                        ReflectionTestUtils.getField(mavenPomCache, "l2").getClass()
                    )
                )
                .containsExactly(InMemoryMavenPomCache.class, RocksdbMavenPomCache.class);
            }
        }

        @Nested
        @SpringBootTest(properties = {"parser.pomCacheEnabled=false"})
        @DirtiesContext
        class WhenPomCacheIsEnabledIsFalse {

            @Autowired
            private MavenPomCache mavenPomCache;

            @Test
            @DisplayName("When pomCacheEnabled is false a InMemoryMavenPomCache should be used")
            void InMemoryMavenPomCacheShouldBeUsed() {
                assertThat(mavenPomCache).isInstanceOf(InMemoryMavenPomCache.class);
            }
        }

    }

    @Nested
    @DirtiesContext
    @SpringBootTest(properties = {"parser.pomCacheEnabled=true"})
    @SetSystemProperty(key = "sun.arch.data.model", value = "32")
    class GivenA32BitSystem {

        @Autowired
        private MavenPomCache mavenPomCache;

        @Test
        @DisplayName("With 32Bit an InMemory pom cache gets used")
        void shouldUseInMemoryMavenPomCache() {
            assertThat(mavenPomCache).isInstanceOf(InMemoryMavenPomCache.class);
        }
    }

    @Nested
    @DirtiesContext
    @Import(CustomCacheConfig.class)
    @SpringBootTest(properties = {"parser.pomCacheEnabled=true"})
    class GivenCustomCacheProvided {

        @Autowired
        private MavenPomCache mavenPomCache;

        @Test
        @DisplayName("The custom pom cache should be used")
        void shouldUseTheProvidedPomCache() {
            assertThat(mavenPomCache).isInstanceOf(CustomPomCache.class);
        }
    }
}

@TestConfiguration
class CustomCacheConfig {
    // Provide custom MavenPomCache as bean
    // Should overwrite the existing MavenPomCache
    @Bean
    public MavenPomCache mavenPomCache() {
        return new CustomPomCache();
    }
}

class CustomPomCache extends InMemoryMavenPomCache {}
