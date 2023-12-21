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
package org.springframework.sbm.build.migration.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.impl.MavenBuildFileRefactoringFactory;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMinimalPomXml extends AbstractAction {

    @Autowired
    @JsonIgnore
    @Setter
    private Configuration configuration;

    @Autowired
    @JsonIgnore
    private RewriteMavenParser rewriteMavenParser;

    @Autowired
    @JsonIgnore
    private MavenBuildFileRefactoringFactory mavenBuildFileRefactoringFactory;

    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    @Override
    public void apply(ProjectContext context) {
        String projectName = context.getProjectRootDirectory().getFileName().toString();
        Map<String, String> params = new HashMap<>();
        params.put("groupId", "com.example.change");
        params.put("artifactId", projectName);
        params.put("version", "0.1.0-SNAPSHOT");

        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("minimal-pom-xml.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String src = writer.toString();
        Parser.Input input = new Parser.Input(Path.of("pom.xml"), () -> new ByteArrayInputStream(src.getBytes(StandardCharsets.UTF_8)));
        Xml.Document maven = rewriteMavenParser
                .parseInputs(List.of(input), null, executionContext).get(0);
        OpenRewriteMavenBuildFile rewriteMavenBuildFile = new OpenRewriteMavenBuildFile(
                context.getProjectRootDirectory(),
                maven, getEventPublisher(), executionContext,
                mavenBuildFileRefactoringFactory.createRefactoring());
        context.getProjectResources().add(rewriteMavenBuildFile);
    }
}
