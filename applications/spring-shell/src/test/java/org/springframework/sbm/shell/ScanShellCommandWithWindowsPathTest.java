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
package org.springframework.sbm.shell;

import org.jline.utils.AttributedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

public class ScanShellCommandWithWindowsPathTest {


    private ScanShellCommand sut;
    private ScanCommand scanCommand;
    private ApplicableRecipeListRenderer applicationRecipeListRenderer;
    private ApplicableRecipeListCommand applicableRecipeListCommand;
    private PreconditionVerificationRenderer preconditionVerificationRenderer;
    private List<Resource> resources;
    private List<Recipe> recipes;
    private ProjectContext projectContext;

    @BeforeEach
    void beforeEach() {
        scanCommand = mock(ScanCommand.class);
        applicationRecipeListRenderer = mock(ApplicableRecipeListRenderer.class);
        applicableRecipeListCommand = mock(ApplicableRecipeListCommand.class);
        ProjectContextHolder contextHolder = mock(ProjectContextHolder.class);
        preconditionVerificationRenderer = mock(PreconditionVerificationRenderer.class);
        ScanCommandHeaderRenderer scanCommandHeaderRenderer = mock(ScanCommandHeaderRenderer.class);
        ConsolePrinter consolePrinter = mock(ConsolePrinter.class);

        resources = List.of();
        recipes = List.of();
        projectContext = mock(ProjectContext.class);

        sut = new ScanShellCommand(scanCommand, applicationRecipeListRenderer, applicableRecipeListCommand, contextHolder, preconditionVerificationRenderer, scanCommandHeaderRenderer, consolePrinter);
    }

    private void recordMocks(String projectRoot, ScanCommand scanCommand, ApplicableRecipeListRenderer applicationRecipeListRenderer, ApplicableRecipeListCommand applicableRecipeListCommand, List<Resource> resources, List<Recipe> recipes, ProjectContext projectContext) {
        when(scanCommand.execute(projectRoot)).thenReturn(projectContext);
        PreconditionVerificationResult result = new PreconditionVerificationResult(Path.of(projectRoot));
        when(applicableRecipeListCommand.execute(projectContext)).thenReturn(recipes);
        when(applicableRecipeListCommand.execute(projectContext)).thenReturn(recipes);
        when(scanCommand.checkPreconditions(projectRoot, resources)).thenReturn(result);
        when(preconditionVerificationRenderer.renderPreconditionCheckResults(result)).thenReturn("");
        when(applicationRecipeListRenderer.render(recipes)).thenReturn(new AttributedString(""));
    }

    @Test
    void windowsPath() {
        String projectRoot = "C:\\windows\\path";
        recordMocks(projectRoot, scanCommand, applicationRecipeListRenderer, applicableRecipeListCommand, resources, recipes, projectContext);
        sut.scan(projectRoot);
        verify(scanCommand).scanProjectRoot(projectRoot);
    }

    @Test
    void backslashPath() {
        PrintStream initialOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        System.setOut(out);
        String projectRoot = "C:\\windows\\path";
        System.out.println(projectRoot);
    }
}
