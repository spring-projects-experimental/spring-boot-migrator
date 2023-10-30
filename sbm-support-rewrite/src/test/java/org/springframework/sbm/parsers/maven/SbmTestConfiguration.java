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
package org.springframework.sbm.parsers.maven;

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.tree.ParsingEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.sbm.parsers.ParserProperties;
import org.springframework.sbm.parsers.RewriteParserConfiguration;
import org.springframework.sbm.scopes.ScanScope;

/**
 * @author Fabian Krüger
 */
@TestConfiguration
@Slf4j
@Import(RewriteParserConfiguration.class)
public class SbmTestConfiguration {

    @Autowired
    private ParserProperties parserProperties;

    @Bean
    MavenConfigFileParser configFileParser() {
        return new MavenConfigFileParser();
    }

    @Bean
    MavenExecutionRequestFactory requestFactory(MavenConfigFileParser configFileParser) {
        return new MavenExecutionRequestFactory(configFileParser);
    }

    @Bean
    MavenExecutor mavenExecutor(MavenExecutionRequestFactory requestFactory, MavenPlexusContainer plexusContainer) {
        return new MavenExecutor(requestFactory, plexusContainer);
    }

    @Bean
    MavenMojoProjectParserFactory projectParserFactory() {
        return new MavenMojoProjectParserFactory(parserProperties);
    }

    @Bean
    MavenPlexusContainer plexusContainer() {
        return new MavenPlexusContainer();
    }

    @Bean
    MavenModelReader modelReader() {
        return new MavenModelReader();
    }

    @Bean
    RewriteMavenProjectParser rewriteMavenProjectParser(MavenPlexusContainer plexusContainer, ParsingEventListener parsingEventListenerAdapter, MavenExecutor mavenExecutor, MavenMojoProjectParserFactory mavenMojoProjectParserFactory, ScanScope scanScope, ConfigurableListableBeanFactory beanFactory, ExecutionContext executionContext) {
        return new RewriteMavenProjectParser(
                plexusContainer,
                parsingEventListenerAdapter,
                mavenExecutor,
                mavenMojoProjectParserFactory,
                scanScope,
                beanFactory,
                executionContext
        );
    }
}
