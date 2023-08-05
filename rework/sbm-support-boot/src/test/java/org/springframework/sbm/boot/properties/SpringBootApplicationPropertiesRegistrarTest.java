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
package org.springframework.sbm.boot.properties;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.properties.tree.Properties;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootApplicationPropertiesRegistrarTest {

    private Path projectRoot = Path.of("./testdir").toAbsolutePath().normalize();
    private String content = "foo=bar\na=b";
    private SpringBootApplicationPropertiesRegistrar sut = new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext());

    @Test
    void shouldHandleReturnsTrueForDefault() {
        RewriteSourceFileHolder<Properties.File> properties = getFileRewriteSourceFileHolder("src/main/resources/application.properties");
        boolean shouldHandle = sut.shouldHandle(properties);
        assertThat(shouldHandle).isTrue();
    }

    @Test
    void shouldHandleReturnsTrueForConfig() {
        RewriteSourceFileHolder<Properties.File> properties = getFileRewriteSourceFileHolder("src/main/resources/config/application.properties");
        boolean shouldHandle = sut.shouldHandle(properties);
        assertThat(shouldHandle).isTrue();
    }

    @Test
    void shouldHandleReturnsFalseForPropertiesOutsideOfSourceDir() {
        RewriteSourceFileHolder<Properties.File> properties = getFileRewriteSourceFileHolder("foo/application.properties");
        boolean shouldHandle = sut.shouldHandle(properties);
        assertThat(shouldHandle).isFalse();
    }

    @Test
    void testWrapRewriteSourceFileHolder() {
        RewriteSourceFileHolder<Properties.File> properties = getFileRewriteSourceFileHolder("src/main/resources/application.properties");
        SpringBootApplicationProperties bootProperties = sut.wrapRewriteSourceFileHolder(properties);
        String foo = bootProperties.getProperty("foo").get();
        assertThat(foo).isEqualTo("bar");
        assertThat(bootProperties.getAbsoluteProjectDir()).isEqualTo(projectRoot);
        assertThat(bootProperties.getSourcePath()).isEqualTo(Path.of("src/main/resources/application.properties"));
    }

    private RewriteSourceFileHolder<Properties.File> getFileRewriteSourceFileHolder(String resourcePath) {
        RewriteSourceFileHolder<Properties.File> properties = new RewritePropertiesParser().parse(projectRoot, Path.of(resourcePath), content);
        return properties;
    }

}