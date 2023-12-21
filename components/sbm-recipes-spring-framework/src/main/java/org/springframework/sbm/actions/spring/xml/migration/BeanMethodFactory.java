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

import com.squareup.javapoet.*;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

import static org.springframework.sbm.actions.spring.xml.migration.Helper.classNameMatches;
import static org.springframework.sbm.actions.spring.xml.migration.Helper.isOfType;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeanMethodFactory {

    private final ListBeanHandler listBeanHandler;
    private final GenericBeanHandler genericBeanHandler;

    void addBeanDefinitionsToType(MigrationContext migrationContext, TypeSpec.Builder typeSpec) {

        // get PropertyPlaceholderConfigurer beans
        List<XmlBeanDef> propertyPlaceholderConfigurerBeans = migrationContext.getPropertyPlaceholderConfigurerBeans();

        // initialize all properties found
        propertyPlaceholderConfigurerBeans.forEach(beanDef -> initialiseProperties(migrationContext, beanDef));

        // get all non-PropertyPlaceholderConfigurer beans
        List<XmlBeanDef> otherBeans = migrationContext.getBeanDefinitions().values()
                .stream()
                .filter(bd -> !classNameMatches(bd, "PropertyPlaceholderConfigurer"))
                .toList();

        // create
        otherBeans.forEach(beanDef -> {
            String pattern = "org.springframework.jdbc.datasource.init.DataSourceInitializer";
            MethodSpec springBeanJavaConfig;
            if (classNameMatches(beanDef, pattern)) {
                // TODO: handle DataSourceInitializer (and other DisposableBeans)
            } else {
                Class<GenericBeanDefinition> cls = GenericBeanDefinition.class;
                if (isOfType(migrationContext, beanDef, cls)) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else if (classNameMatches(beanDef, "org.springframework.format.support.FormattingConversionServiceFactoryBean")) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else if (migrationContext.hasClass("org.springframework.web.servlet.view.View") &&
                        migrationContext.getClass(beanDef.getBeanDefinition().getBeanClassName()).isAssignableFrom(migrationContext.getClass("org.springframework.web.servlet.view.View"))) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else if (migrationContext.getClass(beanDef.getBeanDefinition().getBeanClassName()).isAssignableFrom(FactoryBean.class)) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else if (classNameMatches(beanDef, "org.springframework.format.support.FormattingConversionServiceFactoryBean")) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else if (classNameMatches(beanDef, "org.springframework.beans.factory.config.ListFactoryBean")) {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                } else {
                    springBeanJavaConfig = createBeanConfigurationMethod(migrationContext, beanDef);
                    // TODO handle these:
                    //org.springframework.web.servlet.view.xml.MarshallingView
                    //org.springframework.context.support.ResourceBundleMessageSource
                    //org.springframework.format.support.FormattingConversionServiceFactoryBean
                    //org.springframework.oxm.jaxb.Jaxb2Marshaller
                    //org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
                    log.warn("Can't handle beans of type: " + beanDef.getBeanDefinition().getBeanClassName());
                }
                typeSpec.addMethod(springBeanJavaConfig);
            }
        });

        propertyPlaceholderConfigurerBeans.forEach(beanDef -> {
            createMembersForProperties(typeSpec, migrationContext.getPropertyFiles());
            movePropertiesToApplicationPropertiesFile(migrationContext);
            deletePropertiesFiles(migrationContext);
        });
    }


    private int sortBeanDefinitions(XmlBeanDef xmlBeanDef, XmlBeanDef xmlBeanDef1) {
        if (classNameMatches(xmlBeanDef, "PropertyPlaceholderConfigurer")) {
            if (classNameMatches(xmlBeanDef1, "PropertyPlaceholderConfigurer")) {
                return 0;
            }
            return -1;
        } else {
            return 1;
        }
    }

    private void deletePropertiesFiles(MigrationContext migrationContext) {
        migrationContext.getProjectContext().getProjectResources().stream()
                .filter(pr -> migrationContext.getPropertyFiles().stream()
                        .anyMatch(pf -> pf.getAbsolutePath().toAbsolutePath().equals(pr.getAbsolutePath())))
                .forEach(pr -> pr.delete());
    }

    private void movePropertiesToApplicationPropertiesFile(MigrationContext migrationContext) {
        List<SpringBootApplicationProperties> applicationProperties = migrationContext.getProjectContext().search(new SpringBootApplicationPropertiesResourceListFilter());
        migrationContext.getPropertyFiles().stream()
                .flatMap(pf -> pf.getProperties().values().stream())
                .forEach(p -> applicationProperties.get(0).setProperty(p.getKey().toString(), p.getValue().toString()));
    }

    private void initialiseProperties(MigrationContext migrationContext, XmlBeanDef bd) {
        String[] locations = (String[]) bd.getBeanDefinition().getPropertyValues().get("locations");
        if (locations != null) {
            processPropertyLocations(migrationContext, locations);
        } else {
            TypedStringValue location = (TypedStringValue) bd.getBeanDefinition().getPropertyValues().get("location");
            // FIXME: handle TypedStringValue for all cases
            processPropertyLocation(migrationContext, location.getValue());
        }
    }

    private void processPropertyLocations(MigrationContext migrationContext, String[] locations) {
        Arrays.stream(locations)
                .forEach(location -> {
                    processPropertyLocation(migrationContext, location);
                });
    }

    private void processPropertyLocation(MigrationContext migrationContext, String location) {
        try {
            if(location.contains("classpath:")) {
                // TODO: handle non-classpath resources ?!
                location = location.replace("classpath:", "");
            }
            Path classpath = migrationContext.getProjectContext().getProjectRootDirectory().resolve("src/main/resources");
            Path classPathLocation = classpath.resolve(location).toAbsolutePath();

            PropertiesFile propertiesFile = new PropertiesFile(classPathLocation);
            migrationContext.addPropertiesFile(propertiesFile);
            InputStream resource = migrationContext.getClassLoader().getResourceAsStream(location);
            Properties properties = new Properties();
            properties.load(resource);

            properties.entrySet().stream()
                    .map(p -> new Property(p.getKey(), p.getValue()))
                    .forEach(p -> {
                        Optional<Class<?>> aClass = resolvePropertyType(migrationContext, p);
                        aClass.ifPresent(p::setType);
                        propertiesFile.addProperty(p);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Class<?>> resolvePropertyType(MigrationContext migrationContext, Property p) {
        return migrationContext.getBeanDefinitions().values().stream()
                .map(xmlBeanDef -> xmlBeanDef.getTypeOfProperty(migrationContext.getClassLoader(), p.getFieldName()))
                .flatMap(Optional::stream)
                //.filter(t -> t.isPresent())
                //.map(t -> t.get())
                .findFirst();
    }

    void createMembersForProperties(TypeSpec.Builder typeSpec, Set<PropertiesFile> propertyFiles) {
        propertyFiles.stream()
                .flatMap(pf -> pf.getProperties().values().stream())
                .forEach(es -> {
                    String fieldName = es.getFieldName();
                    // get type of field
                    //bd.getTypeOfProperty(es.getKey());
                    Class<?> fieldType = es.getPropertyType();
                    // get type of field here and use as type of member
                    typeSpec.addField(FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
                            .addAnnotation(
                                    AnnotationSpec.builder(Value.class)
                                            .addMember("value", CodeBlock.builder().add("$S", "${" + es.getKey().toString() + "}").build())
                                            .build()
                            )
                            .build());
                    // TODO: add property to application.properties
                    // TODO: remove properties file
                });
    }

    public MethodSpec createBeanConfigurationMethod(MigrationContext migrationContext, XmlBeanDef xmlBeanDefinition) {
        try {
            if (isListBeanDefinition(xmlBeanDefinition)) {
                return listBeanHandler.createBeanDefinitionMethod(migrationContext, xmlBeanDefinition);
            } else if (Helper.isGenericBeanDefinition(xmlBeanDefinition)) {
                return genericBeanHandler.createBeanDefinitionMethod(migrationContext, xmlBeanDefinition);
            } else {
                throw new RuntimeException("Bean definition of type '" + xmlBeanDefinition.getBeanDefinition().getClass() + "' is not yet supported");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not create Java bean definition for bean '" + xmlBeanDefinition.getName() + "'", e);
        }
    }

    private boolean isListBeanDefinition(XmlBeanDef xmlBeanDefinition) {
        return "org.springframework.beans.factory.config.ListFactoryBean".equals(xmlBeanDefinition.getBeanDefinition().getBeanClassName());
    }

}
