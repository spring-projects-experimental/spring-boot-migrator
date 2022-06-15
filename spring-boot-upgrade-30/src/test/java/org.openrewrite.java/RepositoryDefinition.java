package org.openrewrite.java;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDefinition {

    private String id;
    private String url;
    private String name;
    private String layout;
    private Boolean snapshotsEnabled;
    private String snapshotsChecksumPolicy;
    private String snapShotsUpdatePolicy;
    private Boolean releasesEnabled;
    private String releasesChecksumPolicy;
    private String releasesUpdatePolicy;
}
