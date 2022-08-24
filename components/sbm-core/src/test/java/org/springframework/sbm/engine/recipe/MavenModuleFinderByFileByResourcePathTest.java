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

package org.springframework.sbm.engine.recipe;

import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.common.filter.MavenModuleFinderByFileByResourcePath;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MavenModuleFinderByFileByResourcePathTest {

    @Test
    public void getsEnclosingMavenProject() {
        ProjectResourceSet projectResourceSet = new ProjectResourceSet(
                List.of(
                        file("pom.xml"),
                        file("spring-app/pom.xml"),
                        file("spring-app/src/main/Hello.java")
                )
        );

        MavenModuleFinderByFileByResourcePath target = new MavenModuleFinderByFileByResourcePath(file("spring-app/src/main/Hello.java").getAbsolutePath());

        Path result = target.apply(projectResourceSet);

        assertThat(result.toAbsolutePath().toString()).isEqualTo("/tmp/spring-app");
    }

    @Test
    public void whenItsNotAMultiModuleMavenFile() {

        ProjectResourceSet projectResourceSet = new ProjectResourceSet(
                List.of(
                        file("pom.xml"),
                        file("src/main/Hello.java")
                )
        );


        MavenModuleFinderByFileByResourcePath target = new MavenModuleFinderByFileByResourcePath(file("src/main/Hello.java").getAbsolutePath());
        Path result = target.apply(projectResourceSet);
        assertThat(result.toAbsolutePath().toString()).isEqualTo("/tmp");

    }

    @Test
    public void nestedMultiModuleFiles() {

        ProjectResourceSet projectResourceSet = new ProjectResourceSet(
                List.of(
                        file("pom.xml"),
                        file("spring-app-multi/pom.xml"),
                        file("spring-app-multi/spring-1/src/main/Hello.java"),
                        file("spring-app-multi/spring-1/pom.xml"),
                        file("spring-app-multi/spring-2/src/main/Hello.java"),
                        file("spring-app-multi/spring-2/pom.xml")
                )
        );

        MavenModuleFinderByFileByResourcePath target = new MavenModuleFinderByFileByResourcePath(file("spring-app-multi/spring-2/src/main/Hello.java").getAbsolutePath());

        Path result = target.apply(projectResourceSet);
        assertThat(result.toAbsolutePath().toString()).isEqualTo("/tmp/spring-app-multi/spring-2");

    }

    @Test
    public void multiModuleOnRootLevel() {
        ProjectResourceSet projectResourceSet = new ProjectResourceSet(
                List.of(
                        file("pom.xml"),
                        file("spring-app/pom.xml"),
                        file("spring-app/src/main/Hello.java"),
                        file("spring-app-2/pom.xml"),
                        file("spring-app-2/src/main/Hello.java")
                )
        );

        MavenModuleFinderByFileByResourcePath target = new MavenModuleFinderByFileByResourcePath(file("spring-app-2/src/main/Hello.java").getAbsolutePath());
        Path result = target.apply(projectResourceSet);

        assertThat(result.toAbsolutePath().toString()).isEqualTo("/tmp/spring-app-2");
    }

    @Test
    public void throwsErrorWhenNoResourceIsFound() {

        ProjectResourceSet projectResourceSet = new ProjectResourceSet(List.of());

        MavenModuleFinderByFileByResourcePath target = new MavenModuleFinderByFileByResourcePath(file("hello/world/A.java").getAbsolutePath());

        assertThrows(IllegalStateException.class, () -> target.apply(projectResourceSet));
    }

    private RewriteSourceFileHolder<? extends SourceFile> file(String path) {


        RewriteSourceFileHolder<? extends SourceFile> mockedRewriteSourceFileHolder = mock(RewriteSourceFileHolder.class);
        when(mockedRewriteSourceFileHolder.getAbsolutePath()).thenReturn(Path.of("/tmp/" + path));

        return mockedRewriteSourceFileHolder;
    }
}
