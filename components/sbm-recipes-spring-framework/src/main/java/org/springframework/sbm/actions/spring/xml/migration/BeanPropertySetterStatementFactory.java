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

import com.squareup.javapoet.CodeBlock;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.parallelPrefix;
import static java.util.Arrays.stream;

/**
 * Create a {@code CodeBlock} with initialization of a bean.
 *
 * <pre>{@code
 *     <bean id="anotherService" class="...">
 *         ...
 *         <property name="theList" ref="myList"/>
 *         <property name="favoriteInteger" value="35"/>
 *         <property name="favoriteString" value="Hello"/>
 *     </bean>
 * }</pre>
 *
 * becomes this {@code CodeBlock}:
 *
 * <pre>{@code
 *      anotherService.setTheList(myList());
 *      anotherService.setFavoriteInteger(35);
 *      anotherService.setFavoriteString("Hello");
 * }</pre>
 */
@Component
public class BeanPropertySetterStatementFactory {

    private final Helper helper;

    public BeanPropertySetterStatementFactory() {
        helper = new Helper();
    }

    /**
     * {class of targetObject}.set{variableName}({assignment})
     * @param migrationContext
     * @param variableName name of the variable to reference {@code targetObject}
     * @param assignment for the assignment
     * @param targetObject for the object setter is called on
     */
    public CodeBlock createSetterCalls(MigrationContext migrationContext, String variableName, BeanDefinition assignment, XmlBeanDef targetObject) {
        Class<?> aClass = migrationContext.getClass(targetObject.getBeanDefinition().getBeanClassName());

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        assignment.getPropertyValues().stream()
                .forEach(pv -> {
                    CodeBlock setterCall = createSetterCall(migrationContext, aClass, pv, variableName);
                    codeBlockBuilder.add(setterCall);
                });
        return codeBlockBuilder.build();
    }

    private CodeBlock createSetterCall(MigrationContext migrationContext, Class<?> aClass, PropertyValue pv, String variableName) {

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        String fieldName = Helper.uppercaseFirstChar(pv.getName());

        // {variableName}.set{fieldName}(
        CodeBlock setterCallStart = renderSetterCallStart(variableName, fieldName);
        // ...
        CodeBlock setterCallValue = renderSetterCallValue(migrationContext, aClass, fieldName, pv);
        // )
        CodeBlock setterCallEnd = CodeBlock.of(")");

        return codeBlockBuilder.addStatement(
                CodeBlock.builder()
                        .add(setterCallStart)
                        .add(setterCallValue)
                        .add(setterCallEnd).build()
        ).build();
    }

    private CodeBlock renderSetterCallValue(MigrationContext migrationContext, Class<?> aClass, String fieldName, PropertyValue pv) {
        /*
         *   <bean id="...">
         *       <property name="fieldName" ...>
         *   </bean>
         */


        if (pv.getValue().getClass().isAssignableFrom(RuntimeBeanReference.class)) {
            String beanName = migrationContext.getBeanDefinitions().get(Helper.getBeanName(pv)).getName();
            return createRuntimeBeanReferenceSetter(beanName, pv);
        }

        if (pv.getValue().getClass().isAssignableFrom(TypedStringValue.class)) {
            return createTypedStringValueSetter(aClass, pv, fieldName);
        }

        // handle
        if(pv.getValue().toString().contains("${")) {
            String propertyName = pv.getValue().toString().replace("${", "").replace("}", "");
            String valueMember = migrationContext.getProperty(propertyName).getFieldName();
            return CodeBlock.builder()
                    .add(CodeBlock.of("$L", valueMember))
                    .build();
        }

        else if(pv.getValue().getClass().isAssignableFrom(String.class)) {
            return CodeBlock.builder()
                    .add(CodeBlock.of("$S", pv.getValue()))
                    .build();
        }

        if (pv.getValue().getClass().isAssignableFrom(ManagedSet.class))
        {
            CodeBlock.Builder block = CodeBlock.builder();
            block.add("$T.of(", Set.class);
            // FIXME: this works only for lists of type String
            ManagedSet managedSet = (ManagedSet) pv.getValue();
            AtomicInteger i = new AtomicInteger();
            managedSet.forEach(item -> {

                if(item.getClass().isAssignableFrom(BeanDefinitionHolder.class)) {
                    BeanDefinitionHolder definitionHolder = (BeanDefinitionHolder) item;

                    // we render bean in bean
                    BeanConstructorCallFactory beanConstructorCallFactory = new BeanConstructorCallFactory();
                    CodeBlock constructorCall = beanConstructorCallFactory.createConstructorCall(migrationContext, "", definitionHolder.getBeanDefinition(), definitionHolder.getBeanDefinition().getBeanClassName());
                    block.add("new $T()", migrationContext.getClass(definitionHolder.getBeanDefinition().getBeanClassName()));
                } else {
                    String pattern = "$S";
                    if(i.get() > 0) {
                        pattern = ", $S";
                    }
                    block.add(pattern, item);
                }


                i.getAndIncrement();
            });
            return block.build();
        }

        if (pv.getValue().getClass().isAssignableFrom(ManagedList.class)) {
            CodeBlock.Builder block = CodeBlock.builder();
            block.add("$T.of(", List.class);
            // FIXME: this works only for lists of type String
            ManagedList managedList = (ManagedList) pv.getValue();
            managedList.forEach(i -> {
                if(managedList.indexOf(i) > 0) {
                    block.add(", $S", i);
                } else {
                    block.add("$S", i);
                }
            });
            block.add(")");
            return block.build();
        }

        if (pv.getOriginalPropertyValue().getValue().toString().contains("${")) {
            // handle property
            Object propertyKey = pv.getOriginalPropertyValue().getValue().toString().replace("${", "").replace("}", "");
            Property property = migrationContext.getProperty(propertyKey);
            Class<?> propertyType = migrationContext.getPropertyType(aClass.getName(), pv.getName());
            property.setType(propertyType);

//            Class<?> memberType = pv.
            return CodeBlock.builder()
                    .add(CodeBlock.of("$L", property.getFieldName()))
                    .build();
        }

        if(pv.getValue().getClass().isAssignableFrom(RootBeanDefinition.class)) {
            RootBeanDefinition rootBeanDefinition = (RootBeanDefinition) pv.getValue();
            BeanDefinition beanDefinition = rootBeanDefinition.getDecoratedDefinition().getBeanDefinition();
            return CodeBlock.builder()
                    .add(CodeBlock.of("$S", pv.getValue()))
                    .build();
        }

        throw new RuntimeException("Could not process value of type " + pv.getValue().getClass());
    }

    private CodeBlock renderSetterCallStart(String variableName, String fieldName) {
        CodeBlock.Builder setterBuilder = CodeBlock.builder();
        // {variableName}.set{fieldName}(
        setterBuilder.add("$L.set$L(", variableName, fieldName);
        return setterBuilder.build();
    }

    private CodeBlock createTypedStringValueSetter(Class<?> aClass, PropertyValue pv, String fieldName) {
        // get type of field
        final String setter = "set" + fieldName;
        Optional<Method> optionalMethod = stream(aClass.getMethods())
                .filter(m -> {
                    return m.getName().equals(setter);
                })
                .findFirst();

        if(!optionalMethod.isPresent()) {
            throw new RuntimeException("Could not find setter '"+setter+"(...)' on " + aClass);
        }

        Method setterMethod = optionalMethod.get();

        Class<?> parameterType = setterMethod.getParameterTypes()[0];

        TypedStringValue stringValue = (TypedStringValue) pv.getValue();

        // handle field of type != String
        CodeBlock.Builder setterBuilder = CodeBlock.builder();
        if (parameterType != String.class) {
            setterBuilder
                    .add(CodeBlock.of("$L", pv.getName()))
                    .build();
        }
        // handle field of type == String
        else if (parameterType == String.class) {
            setterBuilder
                    .add(CodeBlock.of("$S", stringValue.getValue()))
                    .build();
        }
        return setterBuilder.build();
    }

    private CodeBlock createRuntimeBeanReferenceSetter(String beanName, PropertyValue pv) {
        // {variableName}())
        return CodeBlock.builder()
                .add(CodeBlock.of("$L()", beanName))
                .build();
    }

    public CodeBlock createListBeanSetterCalls(String beanVariableName, XmlBeanDef xmlBeanDefinition) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        xmlBeanDefinition.getBeanDefinition().getPropertyValues().stream()
                .forEach(pv -> {
                    ManagedList managedList = (ManagedList) xmlBeanDefinition.getBeanDefinition().getPropertyValues().getPropertyValues()[0].getValue();
                    managedList.stream()
                            .forEach(entry -> {
                                String value1 = ((TypedStringValue) entry).getValue();
                                codeBlockBuilder.addStatement(CodeBlock.of("$L.add($S)", beanVariableName, value1));
                            });
                });
        return codeBlockBuilder.build();
    }
}
