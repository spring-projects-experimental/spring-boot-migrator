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
 * Execute Maven goals and prpivide the  provide a {@link Consumer} for
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class MavenExecutor {

    private final MavenExecutionRequestFactory requestFactory;
    private final MavenPlexusContainerFactory containerFactory;

    /**
     * Runs given {@code goals} in Maven and calls {@code eventConsumer} when {@link org.apache.maven.execution.ExecutionListener#projectSucceeded(ExecutionEvent)} is called
     * providing the current {@link MavenSession}.
     */
    void runAfterMavenGoals(Path baseDir, PlexusContainer plexusContainer, List<String> goals, Consumer<ExecutionEvent> eventConsumer) {
        try {
            MavenExecutionRequest request = requestFactory.createMavenExecutionRequest(plexusContainer, baseDir);
            request.setExecutionListener(new AbstractExecutionListener() {
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
            });
            Maven maven = plexusContainer.lookup(Maven.class);
            MavenExecutionResult execute = maven.execute(request);
            if (execute.hasExceptions()) {
                throw new ParsingException("Maven could not run %s on project '%s'".formatted(goals, baseDir), execute.getExceptions());
            }
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    void runAfterMavenGoals(Path baseDir, List<String> goals, Consumer<ExecutionEvent> eventConsumer) {
        PlexusContainer plexusContainer = containerFactory.create(baseDir);
        runAfterMavenGoals(baseDir, plexusContainer, goals, eventConsumer);
    }
}
