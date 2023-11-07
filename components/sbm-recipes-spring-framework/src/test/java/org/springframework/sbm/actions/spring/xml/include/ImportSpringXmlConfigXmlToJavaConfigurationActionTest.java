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
package org.springframework.sbm.actions.spring.xml.include;

import freemarker.template.Configuration;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ImportSpringXmlConfigXmlToJavaConfigurationActionTest {

    @SpringBootApplication
    public static class TestApp {
    }

    @Autowired
    private Configuration configuration;

    @Test
    void oneXmlBeansFile() {
        String pkgName = TestProjectContext.getDefaultPackageName();
        Path projectRootDirectory = Path.of("./fake/projects/something").toAbsolutePath().normalize();

        @Language("xml")
        String xmlSample = """
                <?xml version="1.0" encoding="UTF-8"?>
                <beans xmlns="http://www.springframework.org/schema/beans"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xmlns:context="http://www.springframework.org/schema/context"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
                </beans>
                """;

        @Language("xml")
        String buildFileSource = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <groupId>org.springframework.sbm</groupId>
                    <artifactId>something</artifactId>
                    <version>0.6.1-SNAPSHOT</version>
                    <properties/>
                    <modelVersion>4.0.0</modelVersion>
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework</groupId>
                            <artifactId>spring-context</artifactId>
                            <version>5.3.16</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext ctx = TestProjectContext.buildProjectContext()
            .withProjectRoot(projectRootDirectory)
            .withMavenRootBuildFileSource(buildFileSource)
            .withProjectResource(Path.of("src/main/resources/my-xml/nicebeans.xml"), xmlSample)
            .build();
        
        ImportSpringXmlConfigAction action = new ImportSpringXmlConfigAction();
        action.setFreemarkerConf(configuration);
        action.apply(ctx);
        
        List<? extends JavaSource> resources = ctx.getProjectJavaSources().list();
        assertThat(resources).hasSize(1);
        JavaSource r = resources.get(0);
        Path absolutePathToConfig = projectRootDirectory.resolve("src/main/java/").resolve(pkgName.replace('.', '/')).resolve("SpringContextImportConfig.java");
        assertThat(r.getResource().getAbsolutePath()).isEqualTo(absolutePathToConfig);

        @Language("java")
        String expected = """
                package %s;
                                
                import org.springframework.context.annotation.Configuration;
                import org.springframework.context.annotation.ImportResource;
                                
                @Configuration
                @ImportResource({"classpath:my-xml/nicebeans.xml"})
                public class SpringContextImportConfig {
                }
                """.formatted(pkgName);

        assertThat(r.getResource().print()).isEqualToNormalizingNewlines(expected);
    }

    @Test
    void twoXmlBeansFiles() {
        String pkgName = TestProjectContext.getDefaultPackageName();

        @Language("xml")
        String xmlSample = """
                <?xml version="1.0" encoding="UTF-8"?>
                <beans xmlns="http://www.springframework.org/schema/beans"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xmlns:context="http://www.springframework.org/schema/context"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
                </beans>
                """;

        @Language("xml")
        String xmlSample2 = """
                <?xml version="1.0" encoding="UTF-8"?>
                <beans xmlns="http://www.springframework.org/schema/beans"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xmlns:context="http://www.springframework.org/schema/context"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
                </beans>
                """;

        @Language("xml")
        String mavenRootBuildFileSource = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <groupId>org.springframework.sbm</groupId>
                    <version>0.6.1-SNAPSHOT</version>
                    <modelVersion>4.0.0</modelVersion>

                    <artifactId>something</artifactId>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework</groupId>
                            <artifactId>spring-context</artifactId>
                            <version>5.3.16</version>
                        </dependency>
                    </dependencies>

                </project>""";

        ProjectContext ctx = TestProjectContext.buildProjectContext()
            .withProjectRoot(Path.of("."))
            .withMavenRootBuildFileSource(mavenRootBuildFileSource)
            .withProjectResource(Path.of("src/main/resources/my-xml/nicebeans.xml"), xmlSample)
            .withProjectResource(Path.of("src/main/resources/my-xml/favabeans.xml"), xmlSample2)
            .build();
        
        ImportSpringXmlConfigAction action = new ImportSpringXmlConfigAction();
        action.setFreemarkerConf(configuration);
        action.apply(ctx);
        
        List<? extends JavaSource> resources = ctx.getProjectJavaSources().list();
        assertThat(resources).hasSize(1);
        JavaSource r = resources.get(0);
        assertThat(r.getResource().getAbsolutePath()).isEqualTo(Path.of(".").toAbsolutePath().resolve("src/main/java/").resolve(pkgName.replace(".", "/")).resolve("SpringContextImportConfig.java").normalize());

        @Language("java")
        String expected = """
                package %s;

                import org.springframework.context.annotation.Configuration;
                import org.springframework.context.annotation.ImportResource;

                @Configuration
                @ImportResource({"classpath:my-xml/favabeans.xml", "classpath:my-xml/nicebeans.xml"})
                public class SpringContextImportConfig {
                }
                """.formatted(pkgName);

        assertThat(r.getResource().print()).isEqualToNormalizingNewlines(expected);
    }

    // TODO: more xml files than one
    
    // TODO: xml files, but they don't look like spring config xml.
    
}
