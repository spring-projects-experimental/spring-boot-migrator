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
package org.springframework.sbm.project.parser;

import com.fasterxml.jackson.core.JsonParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.yaml.YamlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ResourceLoader;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenArtifactDownloader;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.build.migration.MavenPomCacheProvider;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.precondition.PreconditionVerifier;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ProjectResourceSetHolder;
import org.springframework.sbm.project.resource.ProjectResourceWrapperRegistry;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.xml.parser.RewriteXmlParser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = {
//        MavenProjectParser.class,
//        ResourceParser.class,
//        JsonParser.class,
//        XmlParser.class,
//        YamlParser.class,
//        PropertiesParser.class,
//        PlainTextParser.class,
//        ResourceParser.ResourceFilter.class,
//        RewriteMavenParser.class,
//        MavenArtifactDownloader.class,
//        ApplicationEventPublisher.class,
//        JavaProvenanceMarkerFactory.class,
//        JavaParser.class,
        MavenConfigHandler.class,
        ProjectContextInitializer.class,
        RewriteMavenArtifactDownloader.class,
        JavaProvenanceMarkerFactory.class,
        BasePackageCalculator.class,
        BasePackageCalculator.class,
        ProjectRootPathResolver.class,
        PreconditionVerifier.class,
        ProjectContextFactory.class,
        MavenPomCacheProvider.class,
        SbmApplicationProperties.class,
        PathScanner.class,
        RewriteJavaParser.class,
        RewritePlainTextParser.class,
        RewriteYamlParser.class,
        RewriteJsonParser.class,
        ResourceParser.class,
        RewritePropertiesParser.class,
        MavenProjectParser.class,
        RewriteMavenParser.class,
        MavenSettingsInitializer.class,
        RewriteXmlParser.class,
        ResourceHelper.class,
        ResourceLoader.class,
        GitSupport.class,
        ScanCommand.class,
        ProjectResourceSetHolder.class,
        JavaRefactoringFactoryImpl.class,
        ProjectResourceWrapperRegistry.class,
        RewriteSourceFileWrapper.class
})
class MavenProjectParserTest {

    @Autowired
    private MavenProjectParser sut;

    @Test
    void testSort() {
        String parent = PomBuilder.buildPom("com.example:parent:1.0").withModules("child").build();
        String child = PomBuilder.buildPom("com.example:parent:1.0", "child").build();


        Assertions.assertThat(true).isFalse();
    }
}