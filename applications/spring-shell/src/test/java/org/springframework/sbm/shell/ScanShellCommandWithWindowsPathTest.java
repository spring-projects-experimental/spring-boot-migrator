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
    private List<Resource> resources;
    private List<Recipe> recipes;
    private ProjectContext projectContext;

    @BeforeEach
    void beforeEach() {
        scanCommand = mock(ScanCommand.class);
        applicationRecipeListRenderer = mock(ApplicableRecipeListRenderer.class);
        applicableRecipeListCommand = mock(ApplicableRecipeListCommand.class);
        ProjectContextHolder contextHolder = mock(ProjectContextHolder.class);
        PreconditionVerificationRenderer preconditionVerificationRenderer = mock(PreconditionVerificationRenderer.class);
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
