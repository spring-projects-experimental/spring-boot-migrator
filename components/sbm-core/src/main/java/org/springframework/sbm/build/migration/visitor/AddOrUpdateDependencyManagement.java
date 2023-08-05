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
package org.springframework.sbm.build.migration.visitor;

import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.format.AutoFormat;
import org.openrewrite.xml.tree.Content;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.xml.tree.Xml.Tag;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AddOrUpdateDependencyManagement extends MavenVisitor<ExecutionContext> {

    /*
     * What does this visitor do?
     *
	<project>
	    <dependencies>...</dependencies> <-- do NOT go in here and change anything

		<dependencyManagement>
			<dependencies>
				<dependency> <-- create this and parents if necessary
					<groupId>${groupId}</groupId>
					<artifactId>${artifactId}</artifactId>
					<version>${artifactId}</version>
					<type>${type}</type>
					<scope>${scope}</scope>
				</dependency>
			</dependencies>
		</dependencyManagement>
     */

    private final String[] expectedTagNames;
    private final Dependency dep;
    private final Tag scope;

    public AddOrUpdateDependencyManagement(Xml.Tag scope, Dependency dep, String... expectedTagNames) {
        this.scope = scope;
        this.dep = dep;
        Assert.isTrue(expectedTagNames.length > 0, "Must have at least one expected tag name to indicate insertion location");
        this.expectedTagNames = expectedTagNames;
    }


    public AddOrUpdateDependencyManagement(Dependency dependency) {
        this(null, dependency, "project", "dependencyManagement", "dependencies");
    }

    @Override
    public Xml visitTag(Xml.Tag tag, ExecutionContext p) {
        tag = (Tag) super.visitTag(tag, p);
        if (scope == null || scope.isScope(tag)) {
            String expectedTag = expectedTagNames[0];
            if (expectedTag.equals(tag.getName())) {
                if (expectedTagNames.length >= 2) {
                    String expectedChild = expectedTagNames[1];
                    tag = ensureChild(tag, expectedChild);
                } else {
                    tag = transform(tag);
                }
            }
        }
        return tag;
    }

    private Xml.Tag transform(Tag targetNode) {

        if (!hasTag(targetNode, dependency ->
                dep.getGroupId().equals(dependency.getChildValue("groupId").orElse(null))
                        && dep.getArtifactId().equals(dependency.getChildValue("artifactId").orElse(null))
                        && (dep.getVersion() == null || dep.getVersion().equals(dependency.getChildValue("version").orElse(null)))
                        && (dep.getScope() == null || dep.getScope().equals(dependency.getChildValue("scope").orElse(null)))
                        && (dep.getType() == null || dep.getType().equals(dependency.getChildValue("type").orElse(null)))
        )) {

            // Exact dependency isn't there. Remove same group/artifact dependency if found
            targetNode = removeChild(targetNode,
                    dependency -> dep.getGroupId().equals(dependency.getChildValue("groupId").orElse(null))
                            && dep.getArtifactId().equals(dependency.getChildValue("artifactId").orElse(null)));

            return addChild(targetNode, Tag.build(
                    "<dependency>\n" +
                            "    <groupId>" + dep.getGroupId() + "</groupId>\n" +
                            "    <artifactId>" + dep.getArtifactId() + "</artifactId>\n" +
                            tagString("version", dep.getVersion()) +
                            tagString("type", dep.getType()) +
                            tagString("scope", dep.getScope()) +
                            "</dependency>\n"
            ));
        } else {
            return targetNode;
        }
    }

    private String tagString(String name, String value) {
        return value == null
                ? ""
                : "    <" + name + ">" + value + "</" + name + ">\n";
    }

    private boolean hasTag(Tag parent, Predicate<Tag> predicate) {
        List<? extends Content> _content = parent.getContent();
        if (_content == null || _content.isEmpty()) {
            return false;
        }
        for (Content piece : _content) {
            if (piece instanceof Tag) {
                Tag child = (Tag) piece;
                if (predicate.test(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Tag removeChild(Tag parent, Predicate<Tag> removeWhen) {
        List<Content> content = new ArrayList<>();
        List<? extends Content> _content = parent.getContent();
        if (_content == null || _content.isEmpty()) {
            return parent;
        }
        for (Content piece : _content) {
            if (piece instanceof Tag) {
                Tag child = (Tag) piece;
                if (removeWhen.test(child)) {
                    //skip
                } else {
                    //keep
                    content.add(child);
                }
            } else {
                content.add(piece);
            }
        }
        return parent.withContent(content);

    }

    private Xml.Tag ensureChild(Tag tag, String childTagName) {
        Tag child = tag.getChildren().stream()
                .filter(t -> childTagName.equals(t.getName()))
                .findFirst()
                .orElse(null);

        if (child == null) {
            child = Xml.Tag.build("<" + childTagName + ">\n</" + childTagName + ">\n");
            tag = addChild(tag, child);
        }
        doAfterVisit(new AddOrUpdateDependencyManagement(child, dep, Arrays.copyOfRange(expectedTagNames, 1, expectedTagNames.length)));
        return tag;
    }

    private Tag addChild(Tag parent, Tag child) {
        child = child.withPrefix("\n");
        List<? extends Content> _content = parent.getContent();
        List<Content> content = _content == null ? new ArrayList<>() : new ArrayList<>(_content);
        content.add(child);
        parent = parent.withContent(content);
        doAfterVisit(new AutoFormat().getVisitor());
        return parent;
    }

}