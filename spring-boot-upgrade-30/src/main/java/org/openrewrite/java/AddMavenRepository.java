package org.openrewrite.java;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;
import java.util.UUID;

@Getter
public class AddMavenRepository extends Recipe {

    @Option(
            displayName = "id",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String id;
    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String url;
    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String repositoryName;

    @Option(
            displayName = "layout",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private String layout;

    @Option(
            displayName = "snapshotsEnabled",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private Boolean snapshotsEnabled;

    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private String snapshotsChecksumPolicy;

    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private String snapShotsUpdatePolicy;

    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private Boolean releasesEnabled;

    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private String releasesChecksumPolicy;

    @Option(
            displayName = "repositoryName",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    @Nullable
    private String releasesUpdatePolicy;

    public String getDisplayName() {
        return "Upgrade Maven parent project version";
    }

    public String getDescription() {
        return "Set the parent pom version number according to a node-style semver selector or to a specific version number.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new AddRepositoryVisitor();
    }

    private class AddRepositoryVisitor extends MavenIsoVisitor<ExecutionContext> {
        private AddRepositoryVisitor() {
        }

        @Override
        public Xml.Document visitDocument(Xml.Document document, ExecutionContext ctx) {
            Xml.Tag parent = document.getRoot();
            Optional<Xml.Tag> repositoriesTag = parent.getChild("repositories");
            if (repositoriesTag.isEmpty()) {
                addRepositoriesTag(parent);
            } else if(noRepositoryWithSameIdExists(repositoriesTag.get())){
                addRepositoryTag(repositoriesTag.get());
            }
            return super.visitDocument(document, ctx);
        }

        private boolean noRepositoryWithSameIdExists(Xml.Tag t) {
            return t.getChildren().stream().anyMatch(repo -> repo.getChildren().stream().anyMatch(c -> c.getName().equals("id") &&
                    false == c.getValue().get().equals(getId())));
        }

        private Xml.Tag addRepositoriesTag(Xml.Tag parent) {
            Xml.Tag repositoriesTag = Xml.Tag.build(
                            "<repositories>\n" +
                            renderRepositoryTag() +
                            "</repositories>\n");
            this.doAfterVisit(new AddToTagVisitor(parent, repositoriesTag));
            return repositoriesTag;
        }

        private void addRepositoryTag(Xml.Tag parent) {
            AddToTagVisitor visitor = new AddToTagVisitor(parent, Xml.Tag.build(renderRepositoryTag()));
            this.doAfterVisit(visitor);
        }

        private String renderRepositoryTag() {
            StringBuilder sb = new StringBuilder();
                sb.append("<repository>\n");
                sb.append("<id>" + getId() + "</id>\n");
                if(AddMavenRepository.this.getRepositoryName() != null) {
                    sb.append("<name>").append(AddMavenRepository.this.getRepositoryName()).append("</name>\n");
                }
                if(getUrl() != null) {
                    sb.append("<url>").append(getUrl()).append("</url>\n");
                }
                if(getReleasesEnabled() != null && getReleasesEnabled() == true) {
                    String releaseSection = renderSection("releases", getReleasesChecksumPolicy(), getReleasesUpdatePolicy());
                    sb.append(releaseSection);
                }
                if(getSnapshotsEnabled() != null && getSnapshotsEnabled() == true) {
                    String snapshotsSection = renderSection("snapshots", getSnapshotsChecksumPolicy(), getSnapShotsUpdatePolicy());
                    sb.append(snapshotsSection);
                }
                sb.append("</repository>\n");
            return sb.toString();
        }

        @NotNull
        private String renderSection(String type, String checksum, String update) {
            StringBuilder sb = new StringBuilder();

            sb.append("<").append(type).append(">\n");
            sb.append("<enabled>true</enabled>\n");
            if (checksum != null) {
                sb.append("<checksumPolicy>").append(checksum).append("</checksumPolicy>\n");
            }
            if (update != null) {
                sb.append("<updatePolicy>").append(update).append("</updatePolicy>\n");
            }
            sb.append("</").append(type).append(">\n");
            return sb.toString();
        }

    }
}
