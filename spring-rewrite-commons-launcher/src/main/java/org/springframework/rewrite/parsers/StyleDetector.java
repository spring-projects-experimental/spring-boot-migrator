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
package org.springframework.rewrite.parsers;

import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.tree.JavaSourceFile;
import org.openrewrite.marker.Marker;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */

public class StyleDetector {

	List<SourceFile> sourcesWithAutoDetectedStyles(Stream<SourceFile> sourceFiles) {
		org.openrewrite.java.style.Autodetect.Detector javaDetector = org.openrewrite.java.style.Autodetect.detector();
		org.openrewrite.xml.style.Autodetect.Detector xmlDetector = org.openrewrite.xml.style.Autodetect.detector();
		List<SourceFile> sourceFileList = sourceFiles.peek(javaDetector::sample).peek(xmlDetector::sample).toList();

		Map<Class<? extends Tree>, NamedStyles> stylesByType = new HashMap<>();
		stylesByType.put(JavaSourceFile.class, javaDetector.build());
		stylesByType.put(Xml.Document.class, xmlDetector.build());

		return ListUtils.map(sourceFileList, applyAutodetectedStyle(stylesByType));
	}

	private UnaryOperator<SourceFile> applyAutodetectedStyle(Map<Class<? extends Tree>, NamedStyles> stylesByType) {
		return (before) -> {
			Iterator var2 = stylesByType.entrySet().iterator();

			while (var2.hasNext()) {
				Map.Entry<Class<? extends Tree>, NamedStyles> styleTypeEntry = (Map.Entry) var2.next();
				if (((Class) styleTypeEntry.getKey()).isAssignableFrom(before.getClass())) {
					before = (SourceFile) before
						.withMarkers(before.getMarkers().add((Marker) styleTypeEntry.getValue()));
				}
			}

			return before;
		};
	}

	// public List<SourceFile> sourcesWithAutoDetectedStyles(Stream<SourceFile>
	// sourceFilesStream) {
	// OpenedRewriteMojo m = new OpenedRewriteMojo();
	// Method method = ReflectionUtils.findMethod(OpenedRewriteMojo.class,
	// "sourcesWithAutoDetectedStyles", Stream.class);
	// ReflectionUtils.makeAccessible(method);
	// return (List<SourceFile>) ReflectionUtils.invokeMethod(method, m,
	// sourceFilesStream);
	// }
	//
	// static class OpenedRewriteMojo extends AbstractRewriteMojo {
	//
	// @Override
	// public void execute() throws MojoExecutionException, MojoFailureException {
	// throw new UnsupportedOperationException();
	// }
	// }

}
