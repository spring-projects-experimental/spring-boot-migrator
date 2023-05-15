package org.springframework.sbm.scopeplayground;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.openrewrite.maven.MavenSettings;

@Getter
@Setter
public class ProjectMetadata {
    private String metadata;
    private MavenSettings mavenSettings;
}