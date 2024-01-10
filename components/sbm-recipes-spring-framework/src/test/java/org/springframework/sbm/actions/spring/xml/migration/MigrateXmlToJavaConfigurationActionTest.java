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

import org.springframework.sbm.build.api.ApplicationModules;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.build.api.Module;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MigrateXmlToJavaConfigurationActionTest {

    @InjectMocks
    MigrateXmlToJavaConfigurationAction sut;

    @Mock
    MigrationContextFactory migrationContextFactory;

    @Mock
    MigrateXmlToJavaConfigurationActionHelper actionHelper;

    @Mock
    XmlToJavaConfigurationMigration springBeanToJavaConfigMigration;

    @Mock
    ProjectContext projectContext;


    @Test
    void testApply() {
        ProjectContext context = mock(ProjectContext.class);

        List<RewriteSourceFileHolder> xmlBeanFiles = List.of();
        MigrationContext migrationContext = mock(MigrationContext.class);
        Module module1 = mock(Module.class);
        Module module2 = mock(Module.class);
        List<Module> modules = List.of(module1, module2);
        ApplicationModules applicationModules = new ApplicationModules(modules);

        when(actionHelper.getXmlBeanDefinitionFiles(context)).thenReturn(xmlBeanFiles);
        when(migrationContextFactory.createMigrationContext(context)).thenReturn(migrationContext);
        when(context.getApplicationModules()).thenReturn(applicationModules);

        sut.apply(context);

        verify(springBeanToJavaConfigMigration).migrateSpringXmlBeanDefinitionsToJavaConfig(migrationContext, module1, xmlBeanFiles);
        verify(springBeanToJavaConfigMigration).migrateSpringXmlBeanDefinitionsToJavaConfig(migrationContext, module2, xmlBeanFiles);
    }

}