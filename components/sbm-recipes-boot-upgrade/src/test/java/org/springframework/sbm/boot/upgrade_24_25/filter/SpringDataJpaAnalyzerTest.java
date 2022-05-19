package org.springframework.sbm.boot.upgrade_24_25.filter;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.MethodCall;
import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SpringDataJpaAnalyzerTest {
    @Test
    void testFindCallsToGetOneMethod() {
        List<MethodCall> foundCalls = List.of();
        ProjectContext context = mock(ProjectContext.class);
        ProjectJavaSources projectJavaSources = mock(ProjectJavaSources.class);
        when(context.getProjectJavaSources()).thenReturn(projectJavaSources);
        when(projectJavaSources.findMethodCalls("org.springframework.data.jpa.repository.JpaRepository getOne(java.lang.Long)")).thenReturn(foundCalls);

        SpringDataJpaAnalyzer sut = new SpringDataJpaAnalyzer();
        List<MethodCall> callsToGetOneMethod = sut.findCallsToGetOneMethod(context);
        assertThat(callsToGetOneMethod).isSameAs(foundCalls);
    }

    @Test
    void testGetJpaRepositoriesWithGetByIdMethod() {
        String tag =
                "package com.example.springboot24to25example;\n" +
                        "\n" +
                        "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n" +
                        "import lombok.Getter;\n" +
                        "import lombok.Setter;\n" +
                        "\n" +
                        "import javax.persistence.Entity;\n" +
                        "import javax.persistence.GeneratedValue;\n" +
                        "import javax.persistence.Id;\n" +
                        "\n" +
                        "@Entity\n" +
                        "@Getter\n" +
                        "@Setter\n" +
                        "@JsonIgnoreProperties({\"hibernateLazyInitializer\", \"handler\"})\n" +
                        "public class Tag {\n" +
                        "    @Id\n" +
                        "    @GeneratedValue\n" +
                        "    private Long id;\n" +
                        "\n" +
                        "    private String tag;\n" +
                        "}";

        String tagRepository =
                "package com.example.springboot24to25example;\n" +
                        "\n" +
                        "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                        "\n" +
                        "public interface TagRepository extends JpaRepository<Tag, Long> {\n" +
                        "    public Tag getOne(Long id);\n" +
                        "}";

        String task =
                "package com.example.springboot24to25example;\n" +
                        "\n" +
                        "import lombok.Getter;\n" +
                        "import lombok.Setter;\n" +
                        "\n" +
                        "import javax.persistence.Entity;\n" +
                        "import javax.persistence.GeneratedValue;\n" +
                        "import javax.persistence.Id;\n" +
                        "\n" +
                        "@Entity\n" +
                        "@Getter\n" +
                        "@Setter\n" +
                        "public class Task {\n" +
                        "\n" +
                        "    @Id\n" +
                        "    @GeneratedValue\n" +
                        "    private Long id;\n" +
                        "\n" +
                        "    private String name;\n" +
                        "\n" +
                        "    private boolean done;\n" +
                        "}";

        String taskRepository =
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                        "import org.springframework.data.jpa.repository.Query;\n" +
                        "import org.springframework.data.repository.query.Param;\n" +
                        "\n" +
                        "public interface TaskRepository extends JpaRepository<Task, Long> {\n" +
                        "\n" +
                        "    @Query(\"from Task t where t.id=:id\")\n" +
                        "    Task getById(@Param(\"id\") Long id);\n" +
                        "\n" +
                        "}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", tag)
                .withJavaSource("src/main/java", tagRepository)
                .withJavaSource("src/main/java", task)
                .withJavaSource("src/main/java", taskRepository)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-data-jpa:2.4.12")
                .build();

        SpringDataJpaAnalyzer sut = new SpringDataJpaAnalyzer();
        List<SpringDataJpaAnalyzer.MatchingMethod> match = sut.getJpaRepositoriesWithGetByIdMethod(context);
        assertThat(match).hasSize(1);
    }
}