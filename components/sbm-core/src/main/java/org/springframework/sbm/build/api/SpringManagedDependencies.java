package org.springframework.sbm.build.api;

import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.MavenRepository;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class holds all the dependencies included in a spring artifact
 */
public class SpringManagedDependencies {

    private static List<MavenRepository> SPRING_REPOSITORIES = List.of(
        new MavenRepository("spring-release", "https://repo.spring.io/release", true, false, null, null)
    );

    private List<org.openrewrite.maven.tree.Dependency> dependencies;
    private static Map<GroupArtifactVersion, SpringManagedDependencies> INSTANCES = new HashMap<>();

    public static SpringManagedDependencies by(String groupId, String artifact, String version){
        final GroupArtifactVersion groupArtifactVersion =
                new GroupArtifactVersion(groupId, artifact, version);

        INSTANCES.computeIfAbsent(groupArtifactVersion, SpringManagedDependencies::new);
        return INSTANCES.get(groupArtifactVersion);
    }

    private SpringManagedDependencies(GroupArtifactVersion groupArtifactVersion){
        dependencies = new MavenPomDownloader(Collections.emptyMap(), new RewriteExecutionContext())
                .download(groupArtifactVersion, null, null, SPRING_REPOSITORIES)
                .getDependencies();
    }

    public Stream<Dependency> stream(){
        return dependencies.stream()
                           .map(d -> Dependency.builder()
                                          .groupId(d.getGroupId())
                                          .artifactId(d.getArtifactId())
                                          .version(d.getVersion())
                                          .build()
                           );
    }


}
