package org.springframework.sbm.parsers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
class MavenExecutorTest {
    @Test
    @DisplayName("Verify MavenSession when running in Maven")
    void verifyMavenSessionWhenRunningInMaven() {
        MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(new MavenConfigFileParser());
        MavenPlexusContainerFactory containerFactory= new MavenPlexusContainerFactory();
        MavenExecutor sut = new MavenExecutor(requestFactory, containerFactory);
        Path baseDir = Path.of("./testcode/maven-projects/maven-config");
        List<String> goals = List.of("clean", "install");
        sut.runAfterMavenGoals(baseDir, goals, event -> {

        });
    }
}