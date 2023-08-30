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
package org.springframework.sbm;/*
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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportRenderer;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Fabian Krüger
 */
@WebMvcTest(controllers = ReportController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplyCommand applyCommand;

    @MockBean
    private ReportHolder reportHolder;

    @MockBean
    private ProjectContextHolder contextHolder;

    @Test
    void testGetRequest() throws Exception {
        ProjectContext projectContext = mock(ProjectContext.class);
        when(contextHolder.getProjectContext()).thenReturn(projectContext);
        mockMvc.perform(get("/spring-boot-upgrade")).andExpect(status().isOk());
        mockMvc.perform(get("/spring-boot-upgrade")).andExpect(status().isOk());
        // For the first request the report is created by the runner, for following calls the report is created again
        verify(applyCommand, times(1)).execute(projectContext, ReportController.REPORT_RECIPE);
    }

    @Test
    void testPostRequest() throws Exception {
        ProjectContext projectContext = mock(ProjectContext.class);
        when(contextHolder.getProjectContext()).thenReturn(projectContext);
        mockMvc.perform(post("/spring-boot-upgrade")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("recipeNames[]", "recipe1", "recipe2")
        ).andExpect(status().isOk());
        verify(applyCommand).execute(projectContext, ReportController.REPORT_RECIPE);
    }
}
