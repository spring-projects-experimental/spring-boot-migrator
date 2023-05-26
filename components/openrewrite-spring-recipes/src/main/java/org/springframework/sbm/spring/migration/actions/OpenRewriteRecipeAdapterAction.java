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
package org.springframework.sbm.spring.migration.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.*;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.yaml.YamlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.GenericTypeListFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class OpenRewriteRecipeAdapterAction extends AbstractAction {
	
    private final Recipe recipe;

    @JsonIgnore
    @Autowired
    private ExecutionContext executionContext;

    public OpenRewriteRecipeAdapterAction(org.openrewrite.Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String getDescription() {
        return recipe.getDescription() == null ? recipe.getDisplayName() : recipe.getDescription();
    }
    
    @Override
    public void apply(ProjectContext context) {

        Map<Path, RewriteSourceFileHolder<?>> projectResources = new HashMap<>();

        context.getProjectResources().stream().forEach(r -> projectResources.put(r.getAbsolutePath(), r));

        List<SourceFile> sourceFiles = new ArrayList<>();
        context.search(new GenericTypeListFilter<>(RewriteSourceFileHolder.class))
                .stream()
                .map(RewriteSourceFileHolder::getSourceFile)
                .filter(r -> SourceFile.class.isAssignableFrom(r.getClass()))
                .forEach(r -> sourceFiles.add(r));

        sourceFiles.addAll(
                new YamlParser()
                        .parse(
                                Stream.concat(allFiles(context.getBuildFile().getResourceFolders()), allFiles(context.getBuildFile().getTestResourceFolders()))
                                        .filter(Objects::nonNull)
                                        .filter(it -> it.getFileName().toString().endsWith(".yml") || it.endsWith(".yaml"))
                                        .collect(Collectors.toList()),
                                null, executionContext
                        )
        );

        sourceFiles.addAll(
                new PropertiesParser()
                        .parse(
                                Stream.concat(allFiles(context.getBuildFile().getResourceFolders()), allFiles(context.getBuildFile().getTestResourceFolders()))
                                        .filter(Objects::nonNull)
                                        .filter(it -> it.getFileName().toString().endsWith(".properties"))
                                        .collect(Collectors.toList()),
                                null,
                                executionContext
                        )
        );

        sourceFiles.addAll(
                new XmlParser()
                        .parse(
                                Stream.concat(allFiles(context.getBuildFile().getResourceFolders()), allFiles(context.getBuildFile().getTestResourceFolders()))
                                        .filter(Objects::nonNull)
                                        .filter(it -> it.getFileName().toString().endsWith(".xml"))
                                        .collect(Collectors.toList()),
                                null,
                                executionContext)
        );

        List<Result> res = recipe.run(sourceFiles).getResults();
		for (Result r : res) {
			replaceWrappedResource(projectResources.get(r.getBefore().getSourcePath()), r);
		}
    }
    
    private <T extends SourceFile> void replaceWrappedResource(RewriteSourceFileHolder<T> resource, Result r) {
		if (resource != null) {
			Class<T> type = (Class<T>) resource.getType();
			if (type.isAssignableFrom(r.getAfter().getClass())) {
				T rewriteResource = type.cast(r.getAfter());
				resource.replaceWith(rewriteResource);
			}
            /*
            else if (type == Properties.class) {
				try {
					Properties p = new Properties();
					p.load(new StringReader(r.getAfter().print()));
					resource.replaceWith((T) p);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else if (type == String.class) {
				resource.replaceWith((T) r.getAfter().print());
			} */

            // FIXME: rewrite this...
            else {
				log.warn("Recipe has changed resource '" + r.getBefore().getSourcePath() + "' but SBM does not support it yet");
			}
		}

    }

    private static Stream<Path> allFiles(List<Path> folders) {
        return folders.stream().flatMap(f -> {
            try {
                return Files.walk(f);
            } catch (IOException e) {
                return Stream.empty();
            }
        });
    }
}
