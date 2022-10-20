package org.springframework.sbm.boot;

import java.util.Optional;

import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.AddProperty;
import org.openrewrite.maven.ChangePropertyValue;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.maven.RemoveProperty;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.format.AutoFormat;
import org.openrewrite.xml.tree.Xml;

import static org.openrewrite.xml.AddToTagVisitor.addToTag;

public class ChangeMavenCompilerPluginConfiguration extends MavenVisitor<ExecutionContext> {

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-compiler-plugin";

	private static final XPathMatcher PLUGINS_MATCHER = new XPathMatcher("/project/build/plugins");

	private static final String MAVEN_COMPILER_SOURCE = "${maven.compiler.source}";

	private static final String MAVEN_COMPILER_TARGET = "${maven.compiler.target}";

	private static final String JAVA_VERSION = "java.version";

	@Override
	public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
		Xml.Tag plugins = (Xml.Tag) super.visitTag(tag, ctx);
		if (!PLUGINS_MATCHER.matches(getCursor())) {
			return plugins;
		}
		Optional<Xml.Tag> maybePlugin = plugins.getChildren().stream()
				.filter(plugin -> "plugin".equals(plugin.getName())
						&& GROUP_ID.equals(plugin.getChildValue("groupId").orElse(null))
						&& ARTIFACT_ID.equals(plugin.getChildValue("artifactId").orElse(null)))
				.findAny();
		if (maybePlugin.isEmpty()) {
			return plugins;
		}

		Xml.Tag plugin = maybePlugin.get();
		Optional<Xml.Tag> maybeConfiguration = maybePlugin.get().getChild("configuration");
		if (maybeConfiguration.isPresent()) {
			Xml.Tag configuration = maybeConfiguration.get();

			String sourceValue = configuration.getChildValue("source").orElse(null);
			String targetValue = configuration.getChildValue("target").orElse(null);

			if (sourceValue != null && targetValue != null) {
				String sourceLookupValue = sourceValue.startsWith("${")
						? super.getResolutionResult().getPom().getValue(sourceValue.trim()) : sourceValue;

				String targetLookupValue = targetValue.startsWith("${")
						? super.getResolutionResult().getPom().getValue(targetValue.trim()) : targetValue;

				// If source and target version are same
				if (sourceLookupValue != null && sourceLookupValue.equals(targetLookupValue)){
					doAfterVisit(new ChangePropertyValue(JAVA_VERSION, sourceLookupValue, true));
					doAfterVisit(new ChangeTagValueVisitor<>(configuration.getChild("source").get(), MAVEN_COMPILER_SOURCE));
					doAfterVisit(new ChangeTagValueVisitor<>(configuration.getChild("target").get(), MAVEN_COMPILER_TARGET));

					if (sourceValue.startsWith("${")) {
						doAfterVisit(new RemoveProperty(sourceValue.substring(2, sourceValue.length() - 1)));
					}
					if (targetValue.startsWith("${")){
						doAfterVisit(new RemoveProperty(targetValue.substring(2, targetValue.length() - 1)));
					}
				}
			}

			if (sourceValue == null ){
				addToTag(configuration,
						Xml.Tag.build("<source>" + MAVEN_COMPILER_SOURCE + "</source>\n")
								.withPrefix("\n"),
						getCursor());
			}

			if (targetValue == null){
				addToTag(configuration,
						Xml.Tag.build("<target>" + MAVEN_COMPILER_TARGET + "</target>\n")
								.withPrefix("\n"),
						getCursor());
			}
		}
		else {
			Xml.Tag configurations = Xml.Tag.build("<configuration>\n<source>" + MAVEN_COMPILER_SOURCE
					+ "</source>\n<target>" + MAVEN_COMPILER_TARGET + "</target>\n</configuration>\n");
			doAfterVisit(new AddToTagVisitor<>(plugin, configurations));
		}
		return plugins;
	}
}
