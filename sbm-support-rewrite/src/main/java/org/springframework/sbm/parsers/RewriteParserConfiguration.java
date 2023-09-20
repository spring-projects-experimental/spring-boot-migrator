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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.cache.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.tree.ParsingEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.sbm.boot.autoconfigure.ParserPropertiesPostProcessor;
import org.springframework.sbm.boot.autoconfigure.ScannerConfiguration;
import org.springframework.sbm.parsers.events.RewriteParsingEventListenerAdapter;
import org.springframework.sbm.scopes.ProjectMetadata;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.sbm.boot.autoconfigure.ScopeConfiguration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;


/**
 * Module configuration.
 *
 * @author Fabian Krüger
 */
@Slf4j
@AutoConfiguration(after = {ScopeConfiguration.class})
@EnableConfigurationProperties(ParserProperties.class)
@Import({ScanScope.class, ScopeConfiguration.class})
public class RewriteParserConfiguration {

    @Autowired
    private ParserProperties parserProperties;

    @Bean
    MavenPlexusContainer plexusContainer() {
        return new MavenPlexusContainer();
    }

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
    ProvenanceMarkerFactory provenanceMarkerFactory(MavenMojoProjectParserFactory projectParserFactory) {
        return new ProvenanceMarkerFactory(projectParserFactory);
    }

    @Bean
    @org.springframework.sbm.scopes.annotations.ScanScope
    JavaParserBuilder javaParserBuilder() {
        return new JavaParserBuilder();
    }

    @Bean
    BuildFileParser buildFileParser() {
        return new BuildFileParser();
    }

    @Bean
    MavenModelReader modelReader() {
        return new MavenModelReader();
    }

    @Bean
    @ConditionalOnMissingBean(MavenArtifactCache.class)
    MavenArtifactCache mavenArtifactCache() {
        return new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository")).orElse(
                new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
        );
    }

    @Bean
    Consumer<Throwable> artifactDownloaderErrorConsumer() {
        return (t) -> {throw new RuntimeException(t);};
    }

    @Bean
    RewriteMavenArtifactDownloader artifactDownloader(MavenArtifactCache mavenArtifactCache, ProjectMetadata projectMetadata, Consumer<Throwable> artifactDownloaderErrorConsumer) {
        return new RewriteMavenArtifactDownloader(mavenArtifactCache, projectMetadata.getMavenSettings(), artifactDownloaderErrorConsumer);
    }

    @Bean
    MavenMojoProjectParserPrivateMethods mavenMojoProjectParserPrivateMethods(MavenMojoProjectParserFactory parserFactory, MavenArtifactDownloader artifactDownloader) {
        return new MavenMojoProjectParserPrivateMethods(parserFactory, artifactDownloader);
    }

    @Bean
    SourceFileParser sourceFileParser(MavenModelReader modelReader, MavenMojoProjectParserPrivateMethods mavenMojoProjectParserPrivateMethods, JavaParserBuilder javaParserBuilder) {
        return new SourceFileParser(parserProperties, mavenMojoProjectParserPrivateMethods, javaParserBuilder);
    }

    @Bean
    StyleDetector styleDetector() {
        return new StyleDetector();
    }

    @Bean
    @ConditionalOnMissingBean(ParsingEventListener.class)
    ParsingEventListener parsingEventListener(ApplicationEventPublisher eventPublisher) {
        return new RewriteParsingEventListenerAdapter(eventPublisher);
    }

    @Bean
    RewriteMavenProjectParser rewriteMavenProjectParser(MavenPlexusContainer plexusContainer, ParsingEventListener parsingListener, MavenExecutor mavenExecutor, MavenMojoProjectParserFactory projectParserFactory, ScanScope scanScope, ConfigurableListableBeanFactory beanFactory, ExecutionContext executionContext) {
        return new RewriteMavenProjectParser(
                plexusContainer,
                parsingListener,
                mavenExecutor,
                projectParserFactory,
                scanScope,
                beanFactory,
                executionContext);
    }

    @Bean
    RewriteProjectParser rewriteProjectParser(
            MavenExecutor mavenExecutor,
            ProvenanceMarkerFactory provenanceMarkerFactory,
            BuildFileParser buildFileParser,
            SourceFileParser sourceFileParser,
            StyleDetector styleDetector,
            ParserProperties parserProperties,
            ParsingEventListener parsingEventListener,
            ApplicationEventPublisher eventPublisher,
            ScanScope scanScope,
            ConfigurableListableBeanFactory beanFactory,
            ProjectScanner projectScanner,
            ExecutionContext executionContext) {
        return new RewriteProjectParser(
                mavenExecutor,
                provenanceMarkerFactory,
                buildFileParser,
                sourceFileParser,
                styleDetector,
                parserProperties,
                parsingEventListener,
                eventPublisher,
                scanScope,
                beanFactory,
                projectScanner,
                executionContext);
    }

    @Bean
    ParserPropertiesPostProcessor parserPropertiesPostProcessor() {
        return new ParserPropertiesPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(MavenPomCache.class)
    MavenPomCache mavenPomCache(ParserProperties parserProperties) {
        MavenPomCache mavenPomCache = new InMemoryMavenPomCache();
        if (parserProperties.isPomCacheEnabled()) {
            if (!"64".equals(System.getProperty("sun.arch.data.model", "64"))) {
                log.warn("parser.isPomCacheEnabled was set to true but RocksdbMavenPomCache is not supported on 32-bit JVM. falling back to InMemoryMavenPomCache");
            } else {
                try {
                    mavenPomCache = new CompositeMavenPomCache(
                            new InMemoryMavenPomCache(),
                            new RocksdbMavenPomCache(Path.of(parserProperties.getPomCacheDirectory()))
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
        return mavenPomCache;
    }
}
