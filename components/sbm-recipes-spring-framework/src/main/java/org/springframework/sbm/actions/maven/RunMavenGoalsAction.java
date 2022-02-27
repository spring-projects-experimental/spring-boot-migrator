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
package org.springframework.sbm.actions.maven;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.maven.shared.invoker.*;
import org.apache.maven.shared.utils.cli.CommandLineException;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RunMavenGoalsAction extends AbstractAction {

    private List<String> goals;

    @Override
    public void apply(ProjectContext context) {
        Path projectRootDirectory = context.getProjectRootDirectory();
        executeMavenGoals(projectRootDirectory, "clean", "compile");
    }

    protected void executeMavenGoals(Path executionDir, String... goals) {
        try {
            Invoker invoker = new DefaultInvoker();
            InvocationRequest request = new DefaultInvocationRequest();
            File pomXml = executionDir.resolve("pom.xml").toFile();
            request.setPomFile(pomXml);
            request.setGoals(List.of(goals));
            InvocationResult invocationResult = invoker.execute(request);
            CommandLineException executionException = invocationResult.getExecutionException();
            int exitCode = invocationResult.getExitCode();
            if (executionException != null  || exitCode == 1) {
                throw new RuntimeException("Maven build 'mvn " + Arrays.asList(goals).stream().collect(Collectors.joining(" ")) + "' failed with exitCode: " + exitCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
