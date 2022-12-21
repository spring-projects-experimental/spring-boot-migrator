package org.springframework.sbm.build.api;

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.MavenRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SpringManagedDependencies {

    private static final String SPRING_BOOT_GROUP = "org.springframework.boot";
    private static final String SPRING_GROUP = "org.springframework";

    private static List<MavenRepository> SPRING_REPOSITORIES = List.of(
        new MavenRepository("spring-release", "https://repo.spring.io/release", true, false, null, null)
    );

    private List<org.openrewrite.maven.tree.Dependency> dependencies;
    private static Map<GroupArtifactVersion, SpringManagedDependencies> INSTANCES = new HashMap<>();

    public static SpringManagedDependencies byBootArtifact(String artifact,String version){
        final GroupArtifactVersion groupArtifactVersion =
                new GroupArtifactVersion(SPRING_BOOT_GROUP, artifact, version);

        INSTANCES.computeIfAbsent(groupArtifactVersion, SpringManagedDependencies::new);
        return INSTANCES.get(groupArtifactVersion);
    }

    public static SpringManagedDependencies byArtifact(String artifact,String version){
        final GroupArtifactVersion groupArtifactVersion =
                new GroupArtifactVersion(SPRING_GROUP, artifact, version);

        INSTANCES.computeIfAbsent(groupArtifactVersion, SpringManagedDependencies::new);
        return INSTANCES.get(groupArtifactVersion);
    }

    private SpringManagedDependencies(GroupArtifactVersion groupArtifactVersion){
        dependencies = new MavenPomDownloader(Collections.emptyMap(), new InMemoryExecutionContext())
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
