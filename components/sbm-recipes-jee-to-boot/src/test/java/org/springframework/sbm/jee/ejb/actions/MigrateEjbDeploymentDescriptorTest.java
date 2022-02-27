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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.ejb.api.EjbJarXml;
import org.springframework.sbm.jee.ejb.resource.JeeEjbJarXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.project.resource.filter.GenericTypeListFilter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateEjbDeploymentDescriptorTest {

    public static final String EJB_CLASS_FQNAME = "com.example.jee.ejb.stateless.local.deploymentdescriptor.NoInterfaceViewBean";
    public static final String EJB_TYPE = "Stateless";
    private static final String EJB_NAME = "noInterfaceView";

    @Test
    void givenDeploymentDescriptorContainsEjbWhenMatchingClassIsFoundThenStatelessAnnotationShouldBeOverwritten() {
        // setup fixture
        String javaSource =
                "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"banana\")\n" +
                        "public class NoInterfaceViewBean {}";

        String expected = "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless(name = \"" + EJB_NAME + "\")\n" +
                "public class NoInterfaceViewBean {}";

        String deploymentDescriptorXml = "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                "      version=\"3.2\">\n" +
                "    <enterprise-beans>\n" +
                "        <session>\n" +
                "    <ejb-name>" + EJB_NAME + "</ejb-name>\n" +
                "    <ejb-class>" + EJB_CLASS_FQNAME + "</ejb-class>\n" +
                "    <session-type>" + EJB_TYPE + "</session-type>\n" +
                "        </session>\n" +
                "    </enterprise-beans>\n" +
                "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), deploymentDescriptorXml)
                .withJavaSources(javaSource)
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .addRegistrar(new JeeEjbJarXmlProjectResourceRegistrar())
                .build();


        // call SUT
        MigrateEjbDeploymentDescriptor sut = new MigrateEjbDeploymentDescriptor();
        sut.apply(projectContext);

        // verify...
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
        List<EjbJarXml> deploymentDescriptors = projectContext.search(new GenericTypeListFilter<>(EjbJarXml.class));
        assertThat(deploymentDescriptors).isEmpty();
    }
}