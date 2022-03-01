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
package org.springframework.sbm.build.migration.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.RemoveContentVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

/**
 * @author Alex Boyko
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoveMavenPlugin extends Recipe {

    private static final XPathMatcher PLUGIN_MATCHER = new XPathMatcher("//plugins/plugin");

    private final String groupId;
    private final String artifactId;

    @Override
    public String getDisplayName() {
        return "Remove plugin " + groupId + ":" + artifactId;
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new RemoveMavenPluginVisitor();
    }

    private class RemoveMavenPluginVisitor extends MavenVisitor<ExecutionContext> {

        @Override
        public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
            if (PLUGIN_MATCHER.matches(getCursor()) && hasGroupAndArtifact(groupId, artifactId)) {
                doAfterVisit(new RemoveContentVisitor<>(tag, true));
            }
            return super.visitTag(tag, ctx);
        }

        private boolean hasGroupAndArtifact(String groupId, String artifactId) {
            Xml.Tag tag = getCursor().getValue();
            return groupId.equals(tag.getChildValue("groupId")) &&
                    tag.getChildValue("artifactId")
                            .map(a -> a.equals(artifactId))
                            .orElse(artifactId == null);
        }

    }
}

