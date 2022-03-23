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

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.Recipe;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScanShellCommandTest {
    @Mock
    ScanCommand scanCommand;

    @Mock
    ApplicableRecipeListCommand applicableRecipeListCommand;

    @Mock
    ApplicableRecipeListRenderer applicableRecipeListRenderer;
    
    @Mock
    ProjectContextHolder contextHolder;

    @Mock
    PreconditionVerificationRenderer preconditionVerificationRenderer;

    @Mock
    private ScanCommandHeaderRenderer scanCommandHeaderRenderer;

    @Mock
    private ConsolePrinter consolePrinter;

    @InjectMocks
    ScanShellCommand sut;

    @Test
    void testScanWithFailingPreconditionChecksShouldPrintResult() {
        AttributedString attributedString = new AttributedStringBuilder().toAttributedString();

        String projectRoot = "/test/projectRoot";
        List<Resource> resources = List.of();

        // scans given dir
        when(scanCommand.scanProjectRoot(projectRoot)).thenReturn(resources);
        // render header
        String header = "header";
        when(scanCommandHeaderRenderer.renderHeader(projectRoot)).thenReturn(header);
        // checking precondition returns error
        PreconditionVerificationResult verificationResult = mock(PreconditionVerificationResult.class);
        when(verificationResult.hasError()).thenReturn(true);
        when(scanCommand.checkPreconditions(projectRoot, resources)).thenReturn(verificationResult);
        // render verification result
        String renderedVerificationResult = new AttributedStringBuilder().style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("green"))).append("result").toAnsi();
        when(preconditionVerificationRenderer.renderPreconditionCheckResults(verificationResult)).thenReturn(renderedVerificationResult);

        AttributedString result = sut.scan(projectRoot);

        ArgumentCaptor<String> capturedOutput = ArgumentCaptor.forClass(String.class);
        verify(consolePrinter).println(capturedOutput.capture());

        // header and validation result rendered
        assertThat(capturedOutput.getValue()).isEqualTo("header\u001B[32mresult\u001B[0m");

        assertThat(result.toAnsi()).isEqualTo(attributedString.toAnsi());
    }

    @Test
    void testScanWithSucceedingPreconditionChecksShouldPrintResultAndRenderApplicableRecipes() {
        String recipeName = "The applicable recipe";
        List<Recipe> recipes = List.of(Recipe.builder().name(recipeName).build());

        String projectRoot = "/test/projectRoot";
        List<Resource> resources = List.of();

        // scans given dir
        when(scanCommand.scanProjectRoot(projectRoot)).thenReturn(resources);
        // render header
        String header = "header";
        when(scanCommandHeaderRenderer.renderHeader(projectRoot)).thenReturn(header);
        // checking precondition succeeds
        PreconditionVerificationResult verificationResult = mock(PreconditionVerificationResult.class);
        when(verificationResult.hasError()).thenReturn(false);
        when(scanCommand.checkPreconditions(projectRoot, resources)).thenReturn(verificationResult);
        // render verification result
        String renderedVerificationResult = new AttributedStringBuilder().style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("green"))).append("result").toAnsi();
        when(preconditionVerificationRenderer.renderPreconditionCheckResults(verificationResult)).thenReturn(renderedVerificationResult);
        // Creates ProjectContext
        ProjectContext projectContext = mock(ProjectContext.class);
        when(scanCommand.execute(projectRoot)).thenReturn(projectContext);
        // find applicable recipes
        when(applicableRecipeListCommand.execute(projectContext)).thenReturn(recipes);
        // render recipe list
        AttributedString applicableRecipes = new AttributedStringBuilder().style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("red"))).append(recipeName).toAttributedString();
        when(applicableRecipeListRenderer.render(recipes)).thenReturn(applicableRecipes);

        AttributedString result = sut.scan(projectRoot);

        // list of recipes returned
        assertThat(result.toAnsi()).isEqualTo("\u001B[91mThe applicable recipe\u001B[0m");
        // header and validation result rendered
        ArgumentCaptor<String> capturedOutput = ArgumentCaptor.forClass(String.class);
        verify(consolePrinter).println(capturedOutput.capture());
        assertThat(capturedOutput.getValue()).isEqualTo("header\u001B[32mresult\u001B[0m");
        // ProjectContext was cached
        verify(contextHolder).setProjectContext(projectContext);
    }

}