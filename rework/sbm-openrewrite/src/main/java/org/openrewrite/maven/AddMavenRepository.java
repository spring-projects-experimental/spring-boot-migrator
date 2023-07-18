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
package org.openrewrite.maven;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.Optional;

public class AddMavenRepository extends Recipe {

    private RepositoryDefinition mavenRepository;

    public AddMavenRepository(RepositoryDefinition repository) {
        this.mavenRepository = repository;
    }

    @Override
    public String getDisplayName() {
        return "Add a repository to Maven build file";
    }

    @Override
    public String getDescription() {
        return "Add a Maven repository to Maven build file.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new AddRepositoryVisitor();
    }

    private class AddRepositoryVisitor extends MavenVisitor<ExecutionContext> {
        private AddRepositoryVisitor() {
        }

        @Override
        public Xml visitDocument(Xml.Document maven, ExecutionContext ctx) {
            Xml.Tag parent = maven.getRoot();
            Optional<Xml.Tag> repositoriesTag = parent.getChild("repositories");
            if (repositoriesTag.isEmpty()) {
                addRepositoriesTag(parent);
            } else if(noRepositoryWithSameIdExists(repositoriesTag.get())){
                addRepositoryTag(repositoriesTag.get());
            }
            return super.visitDocument(maven, ctx);
        }

        private boolean noRepositoryWithSameIdExists(Xml.Tag t) {
            return t.getChildren().stream().anyMatch(repo -> repo.getChildren().stream().anyMatch(c -> c.getName().equals("id") && false == c.getValue().get().equals(mavenRepository.getId())));
        }

        private Xml.Tag addRepositoriesTag(Xml.Tag parent) {
            Xml.Tag repositoriesTag = Xml.Tag.build(
                            "<repositories>\n" +
                            renderRepositoryTag() +
                            "</repositories>\n");
            this.doAfterVisit(new AddToTagVisitor(parent, repositoriesTag, new MavenTagInsertionComparator(parent.getChildren())));
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
