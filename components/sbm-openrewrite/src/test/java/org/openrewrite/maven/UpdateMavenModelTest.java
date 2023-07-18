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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.*;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.GitHubIssue;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class UpdateMavenModelTest {

    @GitHubIssue("https://github.com/openrewrite/rewrite/issues/2624")
    @Test
    void changesInPluginConfigurationAreNotUpdatedByUpdateMavenModel() {
        // Having a pom with configured plugin
        @Language("xml")
        String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0</version>
                    <packaging>pom</packaging>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.apache.maven.plugins</groupId>
                                <artifactId>maven-compiler-plugin</artifactId>
                                <configuration>
                                    <target>${maven.compiler.target}</target>
                                    <source>${maven.compiler.source}</source>
                                </configuration>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                """;
        List<Xml.Document> pomXmls = MavenParser.builder().build().parse(pom);

        // changing a property in the configuration
        Recipe setMavenCompilerPluginSourceTo17 = new Recipe() {
            @Override
            public String getDisplayName() {
                return "";
            }

            @Override
            public TreeVisitor<?, ExecutionContext> getVisitor() {
                return new MavenIsoVisitor<ExecutionContext>() {

                    @Override
                    public Xml.Document visitDocument(Xml.Document document, ExecutionContext executionContext) {
                        Xml.Document d = super.visitDocument(document, executionContext);
                        Xml.Tag sourceTag = d
                                .getRoot()
                                .getChildren("build")
                                .get(0)
                                .getChild("plugins")
                                .get()
                                .getChild("plugin")
                                .get()
                                .getChild("configuration")
                                .get()
                                .getChild("source")
                                .get();
                        doAfterVisit(new ChangeTagValueVisitor<>(sourceTag, "17"));
                        return d;
                    }
                };
            }
        };

        RecipeRun run = setMavenCompilerPluginSourceTo17.run(pomXmls);
        SourceFile after = run.getResults().get(0).getAfter();
        // The XML reflects the change
        assertThat(after.printAll()).isEqualTo(
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0</version>
                    <packaging>pom</packaging>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                    </properties>
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.apache.maven.plugins</groupId>
                                <artifactId>maven-compiler-plugin</artifactId>
                                <configuration>
                                    <target>${maven.compiler.target}</target>
                                    <source>17</source>
                                </configuration>
                            </plugin>
                        </plugins>
                    </build>
                </project>                                                                      
                """);

        // But not the model doesn't
        assertThat(after.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getPlugins().get(0).getConfiguration().toString()).isEqualTo("{\"target\":\"${maven.compiler.target}\",\"source\":\"${maven.compiler.source}\"}");

        // Using UpdateMavenModel to update the model
        SourceFile afterUpdateModel = new Recipe() {
            @Override
            public String getDisplayName() {
                return "";
            }

            @Override
            public TreeVisitor<?, ExecutionContext> getVisitor() {
                return new UpdateMavenModel<>();
            }
        }.run(List.of(after)).getResults().get(0).getAfter();

        // But the change is not reflected in model
        assertThat(afterUpdateModel.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getPlugins().get(0).getConfiguration().toString()).isEqualTo("{\"target\":\"${maven.compiler.target}\",\"source\":\"${maven.compiler.source}\"}");

        // When parsing the modified pom the change ois reflected
        Xml.Document afterReparse = MavenParser.builder().build().parse(afterUpdateModel.printAll()).get(0);
        assertThat(afterReparse.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getPlugins().get(0).getConfiguration().toString()).isEqualTo("{\"target\":\"${maven.compiler.target}\",\"source\":\"17\"}");
    }
}
