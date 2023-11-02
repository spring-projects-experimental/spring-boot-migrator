package org.springframework.sbm.parsers;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.gradle.parser.ProjectParser;
import org.openrewrite.gradle.toolingapi.OpenRewriteModelBuilder;
import org.openrewrite.gradle.toolingapi.parser.GradleProjectData;
import org.openrewrite.style.NamedStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewriteGradleProjectParser {
		
	private static final List<String> PLAIN_TEXT_MASKS = List.of(
	        "**gradlew",
	        "**META-INF/services/**",
	        "**/META-INF/spring.factories",
	        "**/META-INF/spring/**",
	        "**.bash",
	        "**.bat",
	        "**/CODEOWNERS",
	        "**Dockerfile",
	        "**.gitattributes",
	        "**.gitignore",
	        "**.java-version",
	        "**.jsp",
	        "**.ksh",
	        "**.kts",
	        "**/lombok.config",
	        "**.qute.java",
	        "**.sdkmanrc",
	        "**.sh",
	        "**.sql",
	        "**.txt"
	);
	private static final Logger log = LoggerFactory.getLogger(RewriteGradleProjectParser.class);

	
	public Stream<SourceFile> parse(File dir, File buildFile, ExecutionContext context) {
		GradleProjectData projectData = OpenRewriteModelBuilder.forProjectDirectory(GradleProjectData.class, dir, buildFile);
		ProjectParser parser = new ProjectParser(projectData, new ProjectParser.Options() {
			
			@Override
			public List<NamedStyles> getStyles() {
				return Collections.emptyList();
			}
			
			@Override
			public int getSizeThresholdMb() {
				return Integer.MAX_VALUE;
			}
			
			@Override
			public List<String> getPlainTextMasks() {
				return PLAIN_TEXT_MASKS;
			}
			
			@Override
			public boolean getLogCompilationWarningsAndErrors() {
				return false;
			}
			
			@Override
			public List<String> getExclusions() {
				return Collections.emptyList();
			}
		}, log);
		
		return parser.parse(context);
		
	}

}
