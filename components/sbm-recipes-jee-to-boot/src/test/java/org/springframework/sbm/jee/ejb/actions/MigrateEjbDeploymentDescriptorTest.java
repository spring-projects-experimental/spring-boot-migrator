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
package org.springframework.sbm.jee.ejb.actions;

import org.junit.jupiter.api.Test;
import org.springframework.rewrite.project.resource.finder.GenericTypeListFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.ejb.api.EjbJarXml;
import org.springframework.sbm.jee.ejb.resource.JeeEjbJarXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateEjbDeploymentDescriptorTest {

    public static final String EJB_CLASS_FQNAME = "com.example.jee.ejb.stateless.local.deploymentdescriptor.NoInterfaceViewBean";
    public static final String EJB_TYPE = "Stateless";
    private static final String EJB_NAME = "noInterfaceView";
    private static final String EJB_WITH_MAPPED_NAME = "MappedNameView";
    private static final String EJB_WITH_MAPPED_CLASS_FQNAME = "com.example.jee.ejb.stateless.local.deploymentdescriptor.MappedNameView";
    private static final String MAPPED_NAME = "java:comp/env/ejb/MappedNameViewBean";
    private static final String EJB_WITH_REMOTE_INTERFACE_NAME = "RemoteInterfaceView";
    public static final String EJB_WITH_REMOTE_INTERFACE_FQDN = "com.example.jee.ejb.stateless.local.deploymentdescriptor.RemoteInterfaceView";
    private static final String REMOTE_EJB_INTERFACE = "com.example.jee.ejb.stateless.local.deploymentdescriptor.RemoteInterface";
    private static final String EJB_WITH_LOCAL_INTERFACE_NAME = "LocalInterfaceView";
    public static final String EJB_WITH_LOCAL_INTERFACE_FQDN = "com.example.jee.ejb.stateless.local.deploymentdescriptor.LocalInterfaceView";
    private static final String LOCAL_EJB_INTERFACE = "com.example.jee.ejb.stateless.local.deploymentdescriptor.LocalInterface";

    @Test
    void givenDeploymentDescriptorContainsEjbWhenMatchingClassIsFoundThenStatelessAnnotationShouldBeOverwritten() {
        // setup fixture
        String javaSource = "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
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
                .withProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), deploymentDescriptorXml)
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
        List<EjbJarXml> deploymentDescriptors = projectContext.search(new GenericTypeListFinder<>(EjbJarXml.class));
        assertThat(deploymentDescriptors).isEmpty();
    }

    @Test
    void givenDeploymentDescriptorContainsEjbWithMappedName_whenMatchingClassIsFound_thenStatelessAnnotationShouldBeOverwritten() {
        // setup fixture
        String javaSource =
                "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"banana\")\n" +
                        "public class MappedNameView {}";

        String expected = "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless(name = \"" + EJB_WITH_MAPPED_NAME + "\", mappedName = \"" + MAPPED_NAME + "\")\n" +
                "public class MappedNameView {}";

        String deploymentDescriptorXml = "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                "      version=\"3.2\">\n" +
                "    <enterprise-beans>\n" +
                "        <session>\n" +
                "    <ejb-name>" + EJB_WITH_MAPPED_NAME + "</ejb-name>\n" +
                "    <ejb-class>" + EJB_WITH_MAPPED_CLASS_FQNAME + "</ejb-class>\n" +
                "    <mapped-name>" + MAPPED_NAME + "</mapped-name>\n" +
                "    <session-type>" + EJB_TYPE + "</session-type>\n" +
                "        </session>\n" +
                "    </enterprise-beans>\n" +
                "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), deploymentDescriptorXml)
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
        List<EjbJarXml> deploymentDescriptors = projectContext.search(new GenericTypeListFinder<>(EjbJarXml.class));
        assertThat(deploymentDescriptors).isEmpty();
    }

    @Test
    void givenDeploymentDescriptorContainsEjbWithRemoteInterface_whenMatchingClassIsFound_thenStatelessRemoteAnnotationShouldBeGenerated() {
        // setup fixture
        String javaSource =
                "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "public class RemoteInterfaceView implements RemoteInterface{}";

        String expected = "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                "import javax.ejb.Remote;\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless(name = \"" + EJB_WITH_REMOTE_INTERFACE_NAME + "\")\n" +
                "@Remote(" + REMOTE_EJB_INTERFACE + ".class)\n" +
                "public class RemoteInterfaceView implements RemoteInterface {}";

        String deploymentDescriptorXml = "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                "      version=\"3.2\">\n" +
                "    <enterprise-beans>\n" +
                "        <session>\n" +
                "    <ejb-name>" + EJB_WITH_REMOTE_INTERFACE_NAME + "</ejb-name>\n" +
                "    <ejb-class>" + EJB_WITH_REMOTE_INTERFACE_FQDN + "</ejb-class>\n" +
                "    <remote>" + REMOTE_EJB_INTERFACE + "</remote>\n" +
                "    <session-type>" + EJB_TYPE + "</session-type>\n" +
                "        </session>\n" +
                "    </enterprise-beans>\n" +
                "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), deploymentDescriptorXml)
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
        List<EjbJarXml> deploymentDescriptors = projectContext.search(new GenericTypeListFinder<>(EjbJarXml.class));
        assertThat(deploymentDescriptors).isEmpty();
    }

    @Test
    void givenDeploymentDescriptorContainsEjbWithLocalInterface_whenMatchingClassIsFound_thenStatelessLocalAnnotationShouldBeGenerated() {
        // setup fixture
        String javaSource =
                "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "public class LocalInterfaceView implements LocalInterface{}";

        String expected = "package com.example.jee.ejb.stateless.local.deploymentdescriptor;\n" +
                "import javax.ejb.Local;\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless(name = \"" + EJB_WITH_LOCAL_INTERFACE_NAME + "\")\n" +
                "@Local(" + LOCAL_EJB_INTERFACE + ".class)\n" +
                "public class LocalInterfaceView implements LocalInterface {}";

        String deploymentDescriptorXml = "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                "      version=\"3.2\">\n" +
                "    <enterprise-beans>\n" +
                "        <session>\n" +
                "    <ejb-name>" + EJB_WITH_LOCAL_INTERFACE_NAME + "</ejb-name>\n" +
                "    <ejb-class>" + EJB_WITH_LOCAL_INTERFACE_FQDN + "</ejb-class>\n" +
                "    <local>" + LOCAL_EJB_INTERFACE + "</local>\n" +
                "    <session-type>" + EJB_TYPE + "</session-type>\n" +
                "        </session>\n" +
                "    </enterprise-beans>\n" +
                "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), deploymentDescriptorXml)
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
        List<EjbJarXml> deploymentDescriptors = projectContext.search(new GenericTypeListFinder<>(EjbJarXml.class));
        assertThat(deploymentDescriptors).isEmpty();
    }
}