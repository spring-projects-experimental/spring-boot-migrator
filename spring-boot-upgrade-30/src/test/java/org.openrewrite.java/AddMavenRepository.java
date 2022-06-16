package org.openrewrite.java;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.semver.Semver;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;

public class AddMavenRepository extends Recipe {

    private RepositoryDefinition mavenRepository;

    @Option(
            displayName = "Name",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String name;
    @Option(
            displayName = "Url",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String url;
    @Option(
            displayName = "Id",
            description = "The first part of a dependency coordinate 'org.springframework.boot:spring-boot-parent:VERSION'.",
            example = "org.springframework.boot"
    )
    private String id;

    public String getDisplayName() {
        return "Upgrade Maven parent project version";
    }

    public String getDescription() {
        return "Set the parent pom version number according to a node-style semver selector or to a specific version number.";
    }

    public AddMavenRepository(String id, String url, String name) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.mavenRepository = RepositoryDefinition.builder().id(id).url(url).name(name).build();
    }

//
//    public AddMavenRepository(RepositoryDefinition repository) {
//        this.mavenRepository = repository;
//    }

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
            return t.getChildren().stream().anyMatch(repo -> repo.getChildren().stream().anyMatch(c -> c.getName().equals("id") && false == c.getValue().get().equals(mavenRepository.getId())));
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
                sb.append("<id>" + mavenRepository.getId() + "</id>\n");
                if(mavenRepository.getName() != null) {
                    sb.append("<name>").append(mavenRepository.getName()).append("</name>\n");
                }
                if(mavenRepository.getUrl() != null) {
                    sb.append("<url>").append(mavenRepository.getUrl()).append("</url>\n");
                }
                if(mavenRepository.getReleasesEnabled() != null && mavenRepository.getReleasesEnabled() == true) {
                    String releaseSection = renderSection("releases", mavenRepository.getReleasesChecksumPolicy(), mavenRepository.getReleasesUpdatePolicy());
                    sb.append(releaseSection);
                }
                if(mavenRepository.getSnapshotsEnabled() != null && mavenRepository.getSnapshotsEnabled() == true) {
                    String snapshotsSection = renderSection("snapshots", mavenRepository.getSnapshotsChecksumPolicy(), mavenRepository.getSnapShotsUpdatePolicy());
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
