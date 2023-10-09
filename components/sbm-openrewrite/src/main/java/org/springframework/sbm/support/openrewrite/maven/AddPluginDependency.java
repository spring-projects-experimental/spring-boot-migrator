/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.support.openrewrite.maven;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.maven.internal.InsertDependencyComparator;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.xml.tree.Xml.Tag;

@Data
@AllArgsConstructor
/**
 * TODO(497): remove
 * @deprecated Use {@code #org.openrewrite.maven.AddPluginDependency} instead
 */
@Deprecated(forRemoval = true)
public class AddPluginDependency { /*extends Recipe {

    private static final XPathMatcher PLUGIN_MATCHER = new XPathMatcher("/project/build/plugins/plugin");
    private static final String FOUND_DEPENDENCY_MSG = "plugin-dependency-found";

    private String pluginGroupId;
    private String pluginArtifactId;

    private String groupId;
    private String artifactId;
    private String version;

    @Override
    public String getDisplayName() {
        return "Add dependency to plugin " + pluginGroupId + ":" + pluginArtifactId;
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new AddPluginDependencyVisitor();
    }

    private class AddPluginDependencyVisitor extends MavenVisitor {

        @Override
        public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
            if (PLUGIN_MATCHER.matches(getCursor()) && hasGroupAndArtifact(pluginGroupId, pluginArtifactId)) {
                Tag dependencies = tag.getChild("dependencies").orElse(null);
                if (dependencies != null) {
                    doAfterVisit(new DependencyExistVisitor(dependencies));
                } else {
                    doAfterVisit(new AddToTagVisitor<>(tag, Xml.Tag.build("<dependencies/>"), null));
                }
                doAfterVisit(new AddDependencyTagVisitor(tag));
            }
            return super.visitTag(tag, ctx);
        }

        private boolean hasGroupAndArtifact(String groupId, String artifactId) {
            Xml.Tag tag = getCursor().getValue();
            return groupId.equals(tag.getChildValue("groupId").orElse(model.getGroupId())) &&
                    tag.getChildValue("artifactId")
                            .map(a -> a.equals(artifactId))
                            .orElse(artifactId == null);
        }

    }

    private class AddDependencyTagVisitor extends MavenVisitor {

        private final Tag scope;

        public AddDependencyTagVisitor(Tag tag) {
            this.scope = tag;
        }

        @Override
        public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
            // Any tag
            if (getCursor().isScopeInPath(scope)) {
                if ("dependencies".equals(tag.getName())) {
                    if (!Boolean.TRUE.equals(ctx.getMessage(FOUND_DEPENDENCY_MSG))) {
                        Xml.Tag dependencyTag = Xml.Tag.build(
                                "\n<dependency>\n" +
                                        "<groupId>" + groupId + "</groupId>\n" +
                                        "<artifactId>" + artifactId + "</artifactId>\n" +
                                        (version == null ? "" :
                                                "<version>" + version + "</version>\n") +
                                        "</dependency>"
                        );

                        doAfterVisit(new AddToTagVisitor<>(tag, dependencyTag,
                                new InsertDependencyComparator(tag.getChildren(), dependencyTag)));

                    }
                }
            }

            return super.visitTag(tag, ctx);
        }

    }*/

}
