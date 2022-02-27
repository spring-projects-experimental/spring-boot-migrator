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
package org.springframework.sbm.shell;

import org.springframework.sbm.engine.events.FinishedScanningProjectResourceSetEvent;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceSetEvent;
import me.tongfei.progressbar.ProgressBar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;

import java.nio.file.Path;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApplicableRecipeListRendererTest.class)
class ApplicableRecipeListRendererIntegrationTest {

    @SpyBean
    ApplicableRecipeListRenderer.DisplayProgressBar displayProgressBar;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Test
    void shouldShowProgressBar() {
        int numJavaSources = 2;
        Path path1 = Path.of("path1");
        Path path2 = Path.of("path2");
        ProgressBar progressBar = mock(ProgressBar.class);
        doReturn(progressBar).when(displayProgressBar).createProgressBar(any(String.class), anyLong());

        applicationEventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent(numJavaSources));
        applicationEventPublisher.publishEvent(new StartedScanningProjectResourceEvent(path1));
        applicationEventPublisher.publishEvent(new StartedScanningProjectResourceEvent(path2));
        applicationEventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

        verify(progressBar, times(2)).stepBy(1);
        verify(progressBar).close();
    }
}