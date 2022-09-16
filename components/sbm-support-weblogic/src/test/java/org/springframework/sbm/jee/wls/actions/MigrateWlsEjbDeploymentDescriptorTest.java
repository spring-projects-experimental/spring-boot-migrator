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
package org.springframework.sbm.jee.wls.actions;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.wls.JeeWlsEjbJarProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MigrateWlsEjbDeploymentDescriptorTest {
    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/212")
    void shouldAddTransactionalAnnotationAndTimeoutIfAnnotationDoesNotExist() {

        String javaSource =
                "package example;\n" +
                "import javax.ejb.Stateless;\n" +
                "@Stateless(name=\"daFoo\")\n" +
                "public class Foo {}";

        String buildFileSource =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>jee-sample-app</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n" +
                "    <name>jee-sample-app</name>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework</groupId>\n" +
                "            <artifactId>spring-tx</artifactId>\n" +
                "            <version>5.3.9</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.ejb</groupId>\n" +
                "            <artifactId>javax.ejb-api</artifactId>\n" +
                "            <version>3.2</version>" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>jcenter</id>\n" +
                "            <name>jcenter</name>\n" +
                "            <url>https://jcenter.bintray.com</url>\n" +
                "        </repository>\n" +
                "        <repository>\n" +
                "            <id>mavencentral</id>\n" +
                "            <name>mavencentral</name>\n" +
                "            <url>https://repo.maven.apache.org/maven2</url>\n" +
                "        </repository>\n" +
                "    </repositories>" +
                "</project>";

        String wlsDeploymentDescriptor =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<weblogic-ejb-jar xmlns=\"http://www.bea.com/ns/weblogic/90\" xmlns:j2ee=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bea.com/ns/weblogic/90 http://www.bea.com/ns/weblogic/90/weblogic-ejb-jar.xsd\">\n" +
                "    <weblogic-enterprise-bean>\n" +
                "        <ejb-name>daFoo</ejb-name>\n" +
                "        <transaction-descriptor>\n" +
                "            <trans-timeout-seconds>200</trans-timeout-seconds>\n" +
                "        </transaction-descriptor>\n" +
                "    </weblogic-enterprise-bean>\n" +
                "</weblogic-ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource)
                .addProjectResource(Path.of("./src/main/resources/META-INF/weblogic-ejb-jar.xml"), wlsDeploymentDescriptor)
                .withMavenRootBuildFileSource(buildFileSource)
                .addRegistrar(new JeeWlsEjbJarProjectResourceRegistrar())
                .build();


        MigrateWlsEjbDeploymentDescriptor sut = new MigrateWlsEjbDeploymentDescriptor();
        sut.setEventPublisher(mock(ApplicationEventPublisher.class));
        sut.apply(projectContext);

        String resultingJavaSource = projectContext.getProjectJavaSources().list().get(0).getResource().print();
        assertThat(resultingJavaSource).isEqualTo(
                "package example;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless(name = \"daFoo\")\n" +
                "@Transactional(timeout = 200000)\n" +
                "public class Foo {}"
        );

        assertThat(projectContext.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.springframework\\:spring-tx\\:.*")).isTrue();
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/212")
    void shouldOverwriteTimeoutIfAttributeExist() {

        String javaSource =
                "package example;\n" +
                "import javax.ejb.Stateless;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "@Stateless(name=\"daFoo\")\n" +
                "@Transactional(timeout=10)\n" +
                "public class Foo {}";

        String wlsDeploymentDescriptor =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<weblogic-ejb-jar xmlns=\"http://www.bea.com/ns/weblogic/90\" xmlns:j2ee=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bea.com/ns/weblogic/90 http://www.bea.com/ns/weblogic/90/weblogic-ejb-jar.xsd\">\n" +
                "    <weblogic-enterprise-bean>\n" +
                "        <ejb-name>daFoo</ejb-name>\n" +
                "        <transaction-descriptor>\n" +
                "            <trans-timeout-seconds>200</trans-timeout-seconds>\n" +
                "        </transaction-descriptor>\n" +
                "    </weblogic-enterprise-bean>\n" +
                "</weblogic-ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource)
                .addProjectResource(Path.of("./src/main/resources/META-INF/weblogic-ejb-jar.xml"), wlsDeploymentDescriptor)
                .addRegistrar(new JeeWlsEjbJarProjectResourceRegistrar())
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-tx:5.3.9")
                .build();

        MigrateWlsEjbDeploymentDescriptor sut = new MigrateWlsEjbDeploymentDescriptor();
        sut.apply(projectContext);

        String resultingJavaSource = projectContext.getProjectJavaSources().list().get(0).getResource().print();
        assertThat(resultingJavaSource).isEqualTo(
                "package example;\n" +
                "import javax.ejb.Stateless;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "@Stateless(name=\"daFoo\")\n" +
                "@Transactional(timeout=200000)\n" +
                "public class Foo {}"
        );
    }


    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/212")
    void shouldAddTimeoutIfAnnotationExist() {

        String javaSource =
                "package example;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "@Stateless(name=\"daFoo\")\n" +
                        "@Transactional\n" +
                        "public class Foo {}";

        String wlsDeploymentDescriptor =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<weblogic-ejb-jar xmlns=\"http://www.bea.com/ns/weblogic/90\" xmlns:j2ee=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bea.com/ns/weblogic/90 http://www.bea.com/ns/weblogic/90/weblogic-ejb-jar.xsd\">\n" +
                        "    <weblogic-enterprise-bean>\n" +
                        "        <ejb-name>daFoo</ejb-name>\n" +
                        "        <transaction-descriptor>\n" +
                        "            <trans-timeout-seconds>200</trans-timeout-seconds>\n" +
                        "        </transaction-descriptor>\n" +
                        "    </weblogic-enterprise-bean>\n" +
                        "</weblogic-ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource)
                .addProjectResource(Path.of("./src/main/resources/META-INF/weblogic-ejb-jar.xml"), wlsDeploymentDescriptor)
                .addRegistrar(new JeeWlsEjbJarProjectResourceRegistrar())
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-tx:5.3.9")
                .build();

        MigrateWlsEjbDeploymentDescriptor sut = new MigrateWlsEjbDeploymentDescriptor();
        sut.apply(projectContext);

        String resultingJavaSource = projectContext.getProjectJavaSources().list().get(0).getResource().print();
        assertThat(resultingJavaSource).isEqualTo(
                "package example;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "@Stateless(name=\"daFoo\")\n" +
                        "@Transactional(timeout = 200000)\n" +
                        "public class Foo {}"
        );
    }
}