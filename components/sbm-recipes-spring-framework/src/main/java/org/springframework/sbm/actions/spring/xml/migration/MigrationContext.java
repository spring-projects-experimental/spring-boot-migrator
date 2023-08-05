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

import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.sbm.actions.spring.xml.migration.Helper.classNameMatches;

@Getter
public class MigrationContext {
    private final ProjectContext projectContext;
    private final ClassLoader classLoader;
    private final Map<Object, XmlBeanDef> beanDefinitions = new HashMap<>();
    private final Set<PropertiesFile> propertyFiles = new HashSet<>();

    public MigrationContext(ProjectContext context, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.projectContext = context;
    }

    public InputStream getClasspathResource(String filename) {
        return classLoader.getResourceAsStream(filename);
    }

    public List<XmlBeanDef> getPropertyPlaceholderConfigurerBeans() {
        return getBeanDefinitions().values()
                .stream()
                .filter(bd -> classNameMatches(bd, "PropertyPlaceholderConfigurer"))
                .collect(Collectors.toList());
    }

    public Class<?> getClass(String fqName) {
        try {
            return classLoader.loadClass(fqName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class '" + fqName + "' using classloader '" + classLoader + "'", e);
        }
    }

    public Class<?> getPropertyType(String beanClassName, String property) {
        try {
            String methodName = new StringBuilder("set").append(Helper.uppercaseFirstChar(property)).toString();
            Class<?> aClass = getClassLoader().loadClass(beanClassName);
            List<Method> set = Arrays.stream(aClass.getMethods())
                    .filter(m -> methodName.equals(m.getName()))
                    .filter(m -> m.getParameterTypes().length == 1)
                    .collect(Collectors.toList());

            if (set.isEmpty()) {
                throw new RuntimeException("Could not retrieve type of property '" + property + "' for class '" + beanClassName + "' as there's no method '" + methodName + " defined.'");
            }
            if (set.size() > 1) {
                throw new RuntimeException("Could not retrieve type of property '" + property + "' for class '" + beanClassName + "' as there's more than one method '" + methodName + " defined.'");
            }
            return set.get(0).getParameterTypes()[0];

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not retrieve type of property '" + property + "' for class '" + beanClassName + "'", e);
        }
    }

    public void addBeanDefinition(Object key, XmlBeanDef bd) {
        this.beanDefinitions.put(key, bd);
    }

    public Property getProperty(Object value) {
        return propertyFiles.stream()
                .flatMap(pf -> pf.getProperties().values().stream())
                .filter(p -> value.equals(p.getKey()))
                .findFirst().get();
    }

    public void addPropertiesFile(PropertiesFile propertiesFile) {
        this.propertyFiles.add(propertiesFile);
    }

    public boolean hasClass(String s) {
        try {
            this.getClass(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
