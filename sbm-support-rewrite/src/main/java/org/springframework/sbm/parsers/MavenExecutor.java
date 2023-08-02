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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import org.apache.maven.Maven;
import org.apache.maven.execution.*;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Execute Maven goals and provides the current MavenSession to a custom listener.
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class MavenExecutor {

    private final MavenExecutionRequestFactory requestFactory;
    private final PlexusContainerProvider plexusContainerProvider;


    /**
     * Runs given {@code goals} in Maven and calls {@code eventConsumer} when Maven calls {@link org.apache.maven.execution.ExecutionListener#projectSucceeded(ExecutionEvent)}.
     * The {@code eventConsumer} will be provided with the current {@link MavenSession} through the {@link ExecutionEvent}.
     */
    public void onProjectSucceededEvent(Path baseDir, List<String> goals, Consumer<ExecutionEvent> eventConsumer) {
        PlexusContainer plexusContainer = plexusContainerProvider.get();
        AbstractExecutionListener executionListener = new AbstractExecutionListener() {
            @Override
            public void mojoFailed(ExecutionEvent event) {
                super.mojoFailed(event);
                String mojo = event.getMojoExecution().getGroupId() + ":" + event.getMojoExecution().getArtifactId() + ":" + event.getMojoExecution().getGoal();
                throw new RuntimeException("Exception while executing Maven Mojo: " + mojo, event.getException());
            }

            @Override
            public void projectSucceeded(ExecutionEvent event) {
                eventConsumer.accept(event);
            }

            @Override
            public void projectFailed(ExecutionEvent event) {
                super.projectFailed(event);
                throw new RuntimeException("Exception while executing Maven project: " + event.getProject().getName(), event.getException());
            }
        };
        MavenExecutionRequest request = requestFactory.createMavenExecutionRequest(plexusContainer, baseDir);
        request.setGoals(goals);
        request.setExecutionListener(executionListener);
        execute(request);
    }

    /**
     * Executes the {@code request} against Maven.
     *
     * @see MavenExecutionRequestFactory
     */
    public void execute(MavenExecutionRequest request) {
        try {
            PlexusContainer plexusContainer = plexusContainerProvider.get();
            Maven maven = plexusContainer.lookup(Maven.class);
            MavenExecutionResult execute = maven.execute(request);
            if (execute.hasExceptions()) {
                throw new ParsingException("Maven could not run %s on project '%s'".formatted(request.getGoals(), request.getBaseDirectory()), execute.getExceptions());
            }
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }
}
