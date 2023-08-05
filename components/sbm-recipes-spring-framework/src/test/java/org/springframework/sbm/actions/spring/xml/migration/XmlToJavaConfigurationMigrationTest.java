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
package org.springframework.sbm.actions.spring.xml.migration;

import org.springframework.sbm.test.ProjectContextFileSystemTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class XmlToJavaConfigurationMigrationTest {

    /**
     * Test that {@code XmlBeanDefinitionReader} used by {@code SimpleBeanDefinitionRegistry} need a custom classloader.
     *
     * It simulates the scan of another project and verifies that spring classes have access to this project's classpath
     * using a provided ClassLoader which provides access to all jars defined in the project pom and its classes.
     *
     * The test fails if the jars containing the NamespaceHandler for schemas defined in the XML application context are not on classpath.
     * This can be verified by removing the line which provides the paths to all jars from {@code BuildFile}.
     * In this case you'll see some error like this:
     * Unable to locate Spring NamespaceHandler for XML schema namespace [http://www.springframework.org/schema/oxm]
     */
    @Test
    @Disabled("FIXME: succeeds local but fails with NPE in :48 in CI")
    void simpleBeanDefinitionRegistryShouldUseProvidedClassloader() throws IOException {
        // create project context from given project
        ProjectContext projectContext = ProjectContextFileSystemTestSupport.createProjectContextFromDir("spring-xml-java-config/spring-xml-app");

        // Create migration context which holds the custom classloader
        BeanMethodFactory beanMethodFactory = mock(BeanMethodFactory.class);
        XmlToJavaConfigurationMigration sut = new XmlToJavaConfigurationMigration(beanMethodFactory);

        // create MigrationContext
        MigrationContextFactory migrationContextFactory = new MigrationContextFactory();
        MigrationContext migrationContext = migrationContextFactory.createMigrationContext(projectContext);

        // load spring bean definition file using the classloader
        URL springBeanConfig = migrationContext.getClassLoader().getResource("spring-bean-config.xml");
        FileSystemResource xmlResource = new FileSystemResource(springBeanConfig.getFile());

        // Create SimpleBeanDefinitionRegistry which has access to the custom classloader
        SimpleBeanDefinitionRegistry simpleBeanDefinitionRegistry = sut.getSimpleBeanDefinitionRegistry(migrationContext, xmlResource);

        // Get the bean 'springBean' from registry
        BeanDefinition springBean = simpleBeanDefinitionRegistry.getBeanDefinition("springBean");
        assertThat(springBean).isNotNull();

        // Get the bean 'dataSource' from registry
        BeanDefinition springBean2 = simpleBeanDefinitionRegistry.getBeanDefinition("dataSource");
        assertThat(springBean2).isNotNull();
    }

}