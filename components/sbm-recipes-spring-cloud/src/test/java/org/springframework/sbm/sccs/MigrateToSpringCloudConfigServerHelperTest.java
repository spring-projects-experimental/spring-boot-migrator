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
package org.springframework.sbm.sccs;

import org.springframework.sbm.test.ProjectContextFileSystemTestSupport;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.api.SpringProfile;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MigrateToSpringCloudConfigServerHelperTest {

    private MigrateToSpringCloudConfigServerHelper sut = new MigrateToSpringCloudConfigServerHelper(new GitSupport(new ApplicationProperties()));

    @Test
    void findAllSpringProfiles() {
        String javaSource1 =
                "import org.springframework.context.annotation.Profile;\n" +
                        "@Profile(\"foo\")\n" +
                        "public class SomeClass {}";

        String javaSource2 =
                "import org.springframework.context.annotation.Profile;\n" +
                        "@Profile(value=\"bar\")\n" +
                        "public class AnotherClass {}";

        String applicationPropertiesString =
                "property1=foo";

        String cloudProfilePropertiesString =
                "property1=bar";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/resources/application.properties", applicationPropertiesString)
                .addProjectResource("src/main/resources/application-cloud.properties", cloudProfilePropertiesString)
                .withJavaSources(javaSource1, javaSource2)
                .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.5")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .build();

        List<SpringProfile> allSpringProfiles = sut.findAllSpringProfiles(projectContext);

        assertThat(allSpringProfiles).hasSize(4);
        assertThat(allSpringProfiles.get(0).getProfileName()).isEqualTo("default");
        assertThat(allSpringProfiles.get(1).getProfileName()).isEqualTo("cloud");
        assertThat(allSpringProfiles.get(2).getProfileName()).isEqualTo("foo");
        assertThat(allSpringProfiles.get(3).getProfileName()).isEqualTo("bar");
    }

    @Test
    void initializeGitRepository(@TempDir Path tmpDir) {
        Path projectRoot = tmpDir.resolve("project-root");
        Path newProjectDir = sut.initializeSccsProjectDir(projectRoot);
        assertThat(newProjectDir.resolve(".git")).exists();
    }

    @Test
    void copyFiles(@TempDir Path tmpDir) throws IOException {
        ProjectContext projectContext = ProjectContextFileSystemTestSupport.createProjectContextFromDir("sccs-client");
//        ProjectContext projectContext = TestProjectContext.buildProjectContext()
//                .addProjectResource("src/main/resources/application.properties", "")
//                .addProjectResource("src/main/resources/application-foo.properties", "")
//                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
//                .build();


        Path targetDir = Files.createDirectory(tmpDir.resolve("copied-files"));
        sut.copyFiles(projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()), targetDir);

        assertThat(targetDir.resolve("application.properties")).exists();
        assertThat(targetDir.resolve("application-foo.properties")).exists();
    }

    @Test
    void configureSccsConnection() {
        String applicationPropertiesString =
                "property1=foo";

        String cloudProfilePropertiesString =
                "property1=bar";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/resources/application-cloud.properties", cloudProfilePropertiesString)
                .addProjectResource("src/main/resources/application.properties", applicationPropertiesString)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .build();

        sut.configureSccsConnection(projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()));

        SpringBootApplicationProperties applicationProperties1 = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()).stream().filter(p -> p.getAbsolutePath().toString().endsWith("application.properties")).findFirst().get();
        assertThat(applicationProperties1.print()).isEqualTo(
                "property1=foo\n" +
                        "spring.config.import=optional:configserver:http://localhost:8888"
        );
    }

    @Test
    void testDeleteAllButDefaultProperties() {
        SpringBootApplicationProperties props1 = mock(SpringBootApplicationProperties.class);
        SpringBootApplicationProperties props2 = mock(SpringBootApplicationProperties.class);
        SpringBootApplicationProperties props3 = mock(SpringBootApplicationProperties.class);

        when(props1.isDefaultProperties()).thenReturn(false);
        when(props2.isDefaultProperties()).thenReturn(true);
        when(props3.isDefaultProperties()).thenReturn(false);

        List<SpringBootApplicationProperties> properties = List.of(props1, props2, props3);

        sut.deleteAllButDefaultProperties(properties);

        verify(props1).delete();
        verify(props2, never()).delete();
        verify(props3).delete();
    }

    @Test
    void testFindAllSpringApplicationProperties() {
        ProjectContext context = mock(ProjectContext.class);

        sut.findAllSpringApplicationProperties(context);

        verify(context).search(any(SpringBootApplicationPropertiesResourceListFilter.class));
    }

    @Test
    void commitProperties() {
        Path sccsProjectDir = Path.of("project-dir").toAbsolutePath();
        Path props1Path = Path.of("props1.properties").toAbsolutePath();
        Path props2Path = Path.of("props2.properties").toAbsolutePath();

        SpringBootApplicationProperties props1 = mock(SpringBootApplicationProperties.class);
        when(props1.getAbsolutePath()).thenReturn(props1Path);

        SpringBootApplicationProperties props2 = mock(SpringBootApplicationProperties.class);
        when(props2.getAbsolutePath()).thenReturn(props2Path);

        String commitMessage = "Added properties files: props1.properties, props2.properties";
        GitSupport gitSupport = mock(GitSupport.class);

        sut = new MigrateToSpringCloudConfigServerHelper(gitSupport);

        sut.commitProperties(sccsProjectDir, List.of(props1, props2));

        verify(gitSupport).addAllAndCommit(
                sccsProjectDir.toFile(),
                commitMessage,
                List.of(
                        props1Path.toAbsolutePath().toString(),
                        props2Path.toAbsolutePath().toString()
                ),
                List.of());

    }
}