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
package org.springframework.sbm.build.migration.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.maven.internal.MavenXmlMapper;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Content;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin.OpenRewriteMavenPluginExecution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddMavenPlugin extends Recipe {

	private static final XPathMatcher BUILD_MATCHER = new XPathMatcher("/project/build");

	private final OpenRewriteMavenPlugin plugin;

	@Override
	protected TreeVisitor<?, ExecutionContext> getVisitor() {
		return new AddPluginVisitor();
	}

	private class AddPluginVisitor extends MavenVisitor<ExecutionContext> {

	@Override
		public Xml.Document visitDocument(Xml.Document maven, ExecutionContext ctx) {
			Xml.Document m = (Xml.Document) super.visitDocument(maven, ctx);

			Xml.Tag root = maven.getRoot();
			if (!root.getChild("build").isPresent()) {
				List<Content> collect = root.getContent().stream().map(Content.class::cast).collect(Collectors.toList());
				doAfterVisit(new AddToTagVisitor<>(root,
						Xml.Tag.build(
								"<build>\n" + "<plugins>\n" + createPluginTagString() + "</plugins>\n" + "</build>"),
						new MavenTagInsertionComparator(collect)));
			}

			return m;
		}

		public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
			Xml.Tag t = (Xml.Tag) super.visitTag(tag, ctx);
			if (BUILD_MATCHER.matches(this.getCursor())) {
				Optional<Xml.Tag> maybePlugins = t.getChild("plugins");
				if (!maybePlugins.isPresent()) {
					this.doAfterVisit(new AddToTagVisitor(t, Xml.Tag.build("<plugins/>")));
				}
				else {
					Xml.Tag plugins = maybePlugins.get();
					Optional<Xml.Tag> maybePlugin = plugins.getChildren().stream().filter((pluginx) -> {
						return pluginx.getName().equals("plugin")
								&& plugin.getGroupId().equals(pluginx.getChildValue("groupId").orElse(null))
								&& plugin.getArtifactId().equals(pluginx.getChildValue("artifactId").orElse(null));
					}).findAny();
					if (maybePlugin.isPresent()) {
						Xml.Tag plugin = maybePlugin.get();
						if (AddMavenPlugin.this.plugin.getVersion() != null && !AddMavenPlugin.this.plugin.getVersion()
								.equals(plugin.getChildValue("version").orElse(null))) {
							this.doAfterVisit(new ChangeTagValueVisitor(plugin.getChild("version").get(),
									AddMavenPlugin.this.plugin.getVersion()));
						}
					}
					else {
						Xml.Tag pluginTag = Xml.Tag.build(createPluginTagString());
						this.doAfterVisit(new AddToTagVisitor(plugins, pluginTag));
					}
				}
			}

			return t;
		}

	}

	private String createPluginTagString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<plugin>\n");
		sb.append("<groupId>");
		sb.append(plugin.getGroupId());
		sb.append("</groupId>\n");
		sb.append("<artifactId>");
		sb.append(plugin.getArtifactId());
		sb.append("</artifactId>\n");
		sb.append(renderVersion());
		sb.append(renderExecutions());
		sb.append(renderConfiguration());
		sb.append(plugin.getDependencies() != null ? plugin.getDependencies().trim() + "\n" : "");
		sb.append("</plugin>\n");
		return sb.toString();
	}

	private String renderGoal(String goal) {
		return "<goal>" + goal + "</goal>";
	}

	private String renderVersion() {
		return plugin.getVersion() != null ? "<version>" + plugin.getVersion() + "</version>\n" : "";
	}

	private String renderConfiguration(){
		if (plugin.getConfiguration() != null) {
			try {
				String configurationXml = MavenXmlMapper.writeMapper().writerWithDefaultPrettyPrinter()
						.writeValueAsString(plugin.getConfiguration());
				return configurationXml;
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}

	private String renderExecutions() {
		if (plugin.getExecutions() == null || plugin.getExecutions().isEmpty())
			return "";
		String executions = AddMavenPlugin.this.plugin.getExecutions().stream().map(this::renderExecution)
				.collect(Collectors.joining("\n"));
		return "<executions>\n" + executions + "\n</executions>\n";
	}

	private String renderExecution(OpenRewriteMavenPluginExecution execution) {
		return "<execution>\n" + renderId(execution) + renderGoals(execution) + renderPhase(execution)
				+ renderExecutionConfiguration(execution) + "</execution>";
	}

	private String renderExecutionConfiguration(OpenRewriteMavenPluginExecution execution) {
		return execution.getConfiguration() == null ? "" : execution.getConfiguration().trim();
	}

	private String renderId(OpenRewriteMavenPluginExecution execution) {
		return execution.getId() != null && !execution.getId().isBlank() ? "<id>" + execution.getId() + "</id>\n" : "";
	}

	private String renderGoals(OpenRewriteMavenPluginExecution execution) {
		if (execution.getGoals() == null || execution.getGoals().isEmpty())
			return "";
		String goals = execution.getGoals().stream().map(this::renderGoal).collect(Collectors.joining("\n"));
		return "<goals>\n" + goals + "\n</goals>\n";
	}

	private String renderPhase(OpenRewriteMavenPluginExecution execution) {
		return execution.getPhase() == null ? "" : "<phase>" + execution.getPhase() + "</phase>";
	}

	@Override
	public String getDisplayName() {
		return "Add Maven Plugin";
	}

	@Override
	public String getDescription() {
		return getDisplayName();
	}

}
