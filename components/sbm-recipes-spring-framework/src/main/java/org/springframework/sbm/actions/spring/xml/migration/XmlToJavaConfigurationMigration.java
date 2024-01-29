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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.springframework.sbm.build.api.Module;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class XmlToJavaConfigurationMigration {

    private final BeanMethodFactory beanMethodFactory;

    void migrateSpringXmlBeanDefinitionsToJavaConfig(MigrationContext migrationContext, Module module, List<RewriteSourceFileHolder> xmlBeanFiles) {
        Path sourceFolder = module.getBuildFile().getSourceFolders().get(0);
        String packageName = module.getMainJavaSourceSet().getJavaSourceLocation().getPackageName();
        xmlBeanFiles.forEach(xmlBeanFile -> {
            FileSystemResource xmlBeanDefinition = new FileSystemResource(xmlBeanFile.getAbsolutePath());
            Path filename = xmlBeanFile.getAbsolutePath().getFileName();
            String javaBeanConfiguration = migrateXmlBeanDefinitionFile(migrationContext, packageName, xmlBeanDefinition, filename);
            // add to project module
            module.getMainJavaSourceSet().addJavaSource(module.getProjectRootDirectory(), sourceFolder, javaBeanConfiguration, packageName);
            xmlBeanFile.delete();
        });
    }

    @Deprecated
    String migrateToJavaBeanConfiguration(MigrationContext migrationContext, String packageName, Resource resource, Path filename) {
        return migrateXmlBeanDefinitionFile(migrationContext, packageName, resource, filename);
    }

    private String migrateXmlBeanDefinitionFile(MigrationContext migrationContext, String packageName, Resource resource, Path filename) {

        SimpleBeanDefinitionRegistry beanDefinitionRegistry = getSimpleBeanDefinitionRegistry(migrationContext, resource);
        List<XmlBeanDef> xmlBeanDefinitions = Helper.getBeanDefinitions(beanDefinitionRegistry);
        xmlBeanDefinitions.stream().forEach(bd -> migrationContext.addBeanDefinition(bd.getName(), bd));

        TypeSpec.Builder configurationClass = Helper.createConfigurationClassFromFilename(filename.getFileName().toString());
        beanMethodFactory.addBeanDefinitionsToType(migrationContext, configurationClass);
        JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, configurationClass.build());
        return javaFileBuilder.build().toString();
    }

    SimpleBeanDefinitionRegistry getSimpleBeanDefinitionRegistry(MigrationContext migrationContext, Resource resource) {
        // TODO: if no hierarchy of classloaders -> load by using the custom classloader
        SimpleBeanDefinitionRegistry beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanDefinitionRegistry);
        // replace classloader with the classloader for the project to migrate
        xmlBeanDefinitionReader.setBeanClassLoader(migrationContext.getClassLoader());
        xmlBeanDefinitionReader.setResourceLoader(new PathMatchingResourcePatternResolver(migrationContext.getClassLoader()));
        xmlBeanDefinitionReader.setValidating(false);
        xmlBeanDefinitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        return beanDefinitionRegistry;
    }

}
