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
package org.springframework.sbm.sccs;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MigrateToSpringCloudConfigServerTest {

    @Mock
    private GitSupport gitSupport;

    @Mock
    private MigrateToSpringCloudConfigServerHelper helper;

    @InjectMocks
    private MigrateToSpringCloudConfigServer sut;

    @Test
    void apply() {
        ProjectContext projectContext = mock(ProjectContext.class);
        Path projectRootDir = Path.of("projectRootDir");
        when(projectContext.getProjectRootDirectory()).thenReturn(projectRootDir);
        Path sccsProjectDir = Path.of("sccsProjectDir");
        List<SpringBootApplicationProperties> bootApplicationProperties = List.of();

        when(helper.findAllSpringApplicationProperties(projectContext)).thenReturn(bootApplicationProperties);
        when(helper.initializeSccsProjectDir(projectRootDir)).thenReturn(sccsProjectDir);

        sut.apply(projectContext);

        verify(helper).copyFiles(bootApplicationProperties, sccsProjectDir);
        verify(helper).commitProperties(sccsProjectDir, bootApplicationProperties);
        verify(helper).configureSccsConnection(bootApplicationProperties);
        verify(helper).deleteAllButDefaultProperties(bootApplicationProperties);
    }
}