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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.cache.CompositeMavenPomCache;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.maven.cache.RocksdbMavenPomCache;
import org.openrewrite.xml.tree.Xml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.parsers.*;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;


/**
 * Module configuration.
 *
 * @author Fabian Krüger
 */
@Slf4j
@SpringBootApplication
public class RewriteParserConfig {

    @Bean
    @ConditionalOnMissingBean(MavenPomCache.class)
    MavenPomCache mavenPomCache(ExecutionContext executionContext, ParserSettings parserSettings) {
        MavenPomCache mavenPomCache = new InMemoryMavenPomCache();
        if (parserSettings.isPomCacheEnabled()) {
            if (!"32".equals(System.getProperty("sun.arch.data.model", "64"))) {
                log.warn("parser.isPomCacheEnabled was set to true but RocksdbMavenPomCache is not supported on 32-bit JVM. falling back to InMemoryMavenPomCache");
            } else {
                try {
                    mavenPomCache = new CompositeMavenPomCache(
                            new InMemoryMavenPomCache(),
                            new RocksdbMavenPomCache(Path.of(parserSettings.getPomCacheDirectory()))
                    );
                } catch (Exception e) {
                    log.warn("Unable to initialize RocksdbMavenPomCache, falling back to InMemoryMavenPomCache");
                    if (log.isDebugEnabled()) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();
                        log.debug(exceptionAsString);
                    }
                }
            }
        }
        MavenExecutionContextView.view(executionContext).setPomCache(mavenPomCache);
        return mavenPomCache;
    }
}
