package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.maven.*;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpgradeRepositoryAndPomTest {

    @Test
    @Disabled("Must be activated after fixing model refresh issue in OpenRewrite recipe")
    public void shouldUpdateRepositoryAndParentPom() {
        Recipe repositoryRecipe = new AddMavenRepository(RepositoryDefinition.builder()
                .id("spring-milestone")
                .url("https://repo.spring.io/milestone").build());

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            ex.printStackTrace();
            errors.add(ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>test</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                                
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.7.1</version>
                        <relativePath/> <!-- lookup parent from repository -->
                    </parent>
                </project>
                """);

        List<Result> resultRepository = repositoryRecipe.run(documentList, ctx);
        assertThat(resultRepository).hasSize(1);
        assertThat(((Xml.Document)resultRepository.get(0).getAfter())
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get().getPom().getRepositories()).hasSize(1);
    }
}
