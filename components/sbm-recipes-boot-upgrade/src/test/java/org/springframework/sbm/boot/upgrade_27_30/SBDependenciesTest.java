package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SBDependenciesTest {

    @Test
    public void getSBDependencies() {
        Map<Path, Pom> poms = new HashMap<>();
        MavenPomDownloader downloader = new MavenPomDownloader(poms, new InMemoryExecutionContext());
        GroupArtifactVersion gav = new GroupArtifactVersion("org.springframework.boot", "spring-boot-dependencies", "3.0.0-M3");
        String relativePath = "";
        ResolvedPom containingPom = null;
        Pom pom = downloader.download(gav, relativePath, containingPom, List.of());
        ResolvedPom resolvedPom = pom.resolve(List.of(), downloader, new InMemoryExecutionContext());
        List<ResolvedManagedDependency> dependencyManagement = resolvedPom.getDependencyManagement();
        List<String> versions = dependencyManagement.stream()
                .filter(d -> d.getArtifactId().equals("metrics-annotation"))
                .map(d -> d.getVersion()).collect(Collectors.toList());
        assertThat(pom).isNotNull();
        assertThat(versions).isNotEmpty();
        System.out.println(versions);

//        MavenArtifactDownloader
    }
}
