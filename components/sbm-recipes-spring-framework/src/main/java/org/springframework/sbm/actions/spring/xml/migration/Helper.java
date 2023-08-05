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

import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component // FIXME: no member, all methods can be static (?) -> no component
class Helper {
    static String lowercaseFirstChar(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    static String uppercaseFirstChar(String name) {
        if(name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    // TODO: Move to helper class
    static boolean classNameMatches(XmlBeanDef xmlBeanDef, String pattern) {
        return xmlBeanDef.getBeanDefinition().getBeanClassName().contains(pattern);
    }

    static String calculateClassname(String filename) {
        String name = filename.substring(0, filename.lastIndexOf("."));
        name = splitAndConcat(name, "-");
        name = splitAndConcat(name, "_");
        if (name.contains("-")) {
            name = name.replace("-", "");
        }
        return uppercaseFirstChar(name);
    }

    public static String calculateVariableName(XmlBeanDef xmlBeanDef) {
        String beanName = xmlBeanDef.getName();
        String beanClassName = xmlBeanDef.getBeanDefinition().getBeanClassName();
        return lowercaseFirstChar(beanName); //beanClassName.substring(beanClassName.lastIndexOf(".")+1));
    }

    static String getBeanName(PropertyValue pv) {
        if (pv.getValue().getClass().isAssignableFrom(RuntimeBeanReference.class)) {
            return ((RuntimeBeanReference) pv.getValue()).getBeanName();
        } else if (pv.getValue().getClass().isAssignableFrom(ManagedList.class)) {
            return ((ManagedList) pv.getValue()).getElementTypeName();
        }
        throw new RuntimeException("Could not resolve bean name for " + pv.getName());
    }

    static String getValue(ConstructorArgumentValues.ValueHolder s) {
        if (s.getValue().getClass().isAssignableFrom(TypedStringValue.class)) {
            return ((TypedStringValue) s.getValue()).getValue();
        } else if (s.getValue().getClass().isAssignableFrom(RuntimeBeanReference.class)) {
            return ((RuntimeBeanReference) s.getValue()).getBeanName();
        }
        return "";
    }

    static TypeSpec.Builder createConfigurationClassFromFilename(String filename) {
        String className = calculateClassname(filename);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Configuration.class);
        return typeSpec;
    }

    static List<XmlBeanDef> getBeanDefinitions(SimpleBeanDefinitionRegistry beanDefinitionRegistry) {
        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();
        List<XmlBeanDef> xmlBeanDefinitions = stream(beanDefinitionNames)
                .map(name -> createBeanDefinition(beanDefinitionRegistry, name))
                .filter(bd -> isGenericBeanDefinition(bd))
                .collect(Collectors.toList());
        return xmlBeanDefinitions;
    }

    private static XmlBeanDef createBeanDefinition(SimpleBeanDefinitionRegistry beanDefinitionRegistry, String name) {
        return new XmlBeanDef(name, beanDefinitionRegistry.getBeanDefinition(name));
    }

    static boolean isGenericBeanDefinition(XmlBeanDef xmlBeanDefinition) {
        return xmlBeanDefinition.getBeanDefinition().getClass().isAssignableFrom(GenericBeanDefinition.class);
    }

    static boolean isOfType(MigrationContext migrationContext, XmlBeanDef xmlBeanDef, Class<GenericBeanDefinition> cls) {
        return migrationContext.getClass(xmlBeanDef.getBeanDefinition().getBeanClassName()).isAssignableFrom(cls);
    }

    public static String calculateMethodName(String name) {
        if(name.contains(".")) {
            name = name.substring(name.lastIndexOf("."));
        }
        String splitBy = "\\.";
        String methodNameUppercaseFirst = name.replaceAll("#.*","");
        methodNameUppercaseFirst = splitAndConcat(methodNameUppercaseFirst, splitBy);
        methodNameUppercaseFirst = splitAndConcat(methodNameUppercaseFirst, "/");
        methodNameUppercaseFirst = methodNameUppercaseFirst.replace("@", "").replace("#", "").replace("$", "");
        return Helper.lowercaseFirstChar(methodNameUppercaseFirst);
    }

    private static String splitAndConcat(String name, String splitBy) {
        String methodNameUppercaseFirst = stream(name.split(splitBy))
                .map(token -> Helper.uppercaseFirstChar(token))
                .collect(Collectors.joining());
        return methodNameUppercaseFirst;
    }
}
