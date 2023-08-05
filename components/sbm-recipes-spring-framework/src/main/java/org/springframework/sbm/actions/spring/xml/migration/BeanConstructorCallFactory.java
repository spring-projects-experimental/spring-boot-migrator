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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BeanConstructorCallFactory {

    private final Helper helper = new Helper();

    /**
     * Creates a {@code CodeBlock} with a constructor call statement.
     *
     * <pre>{@code
     *     <bean name="country" class="org.springframework.sbm.actions.spring.xml.example.TestBean">
     *         <constructor-arg index="0" value="India"></constructor-arg>
     *         <constructor-arg index="1" value="20000"></constructor-arg>
     *     </bean>
     * }</code>
     * becomes this {@code CodeBlock}:
     * <pre>{@code
     *      TestBean country = new TestBean("India", "20000")
     * }</pre>
     *
     * <pre>{@code
     *   <bean id="anotherService" class="org.springframework.sbm.actions.spring.xml.example.AnotherServiceImpl">
     *         <constructor-arg ref="appleService"/>
     *         ...
     *   </bean>
     * }</code>
     * becomes this {@code CodeBlock}:
     * <pre>{@code
     *      AnotherServiceImpl anotherService = new AnotherServiceImpl(appleService())
     *  }</code>
     */
    CodeBlock createConstructorCall(MigrationContext migrationContext, String beanVariableName, BeanDefinition beanDefinition, String beanClassName) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        Class<?> beanClazz = null;

        beanClazz = migrationContext.getClass(beanClassName);

        CodeBlock.Builder builder = codeBlockBuilder
                .add("$T $L = new $T(", beanClazz, beanVariableName, beanClazz); // A a = new A(
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

        List<ConstructorArgumentValues.ValueHolder> genericArgumentValues = constructorArgumentValues.getGenericArgumentValues();

        if (!genericArgumentValues.isEmpty()) {
            for (ConstructorArgumentValues.ValueHolder entry : genericArgumentValues) {
                String name= "";
                if(entry.getValue().getClass().isAssignableFrom(TypedStringValue.class )) {
                    name = ((TypedStringValue) entry.getValue()).getValue();
                    builder.add(CodeBlock.of("$L()", name));
                } else if(entry.getValue().getClass().isAssignableFrom(RuntimeBeanReference.class )) {
                    name = ((RuntimeBeanReference) entry.getValue()).getBeanName();
                } else {
                    throw new RuntimeException("Can not handle generic constructor argument of type '" + entry.getValue() + "'. Given ConstructorArgumentValues.ValueHolder '"+ entry +"'.");
                }
                builder.add(CodeBlock.of("$L()", name));
            }
        }


        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();
        if (!indexedArgumentValues.isEmpty()) {
            Set<Map.Entry<Integer, ConstructorArgumentValues.ValueHolder>> entries = indexedArgumentValues.entrySet();
            for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : entries) {
                if (/*entry.getKey() > 0 && */entry.getKey() < entries.size() - 1) {
                    builder.add(CodeBlock.of("$S, ", Helper.getValue(entry.getValue())));
                } else {
                    builder.add(CodeBlock.of("$S", Helper.getValue(entry.getValue())));
                }
            }
        }
        builder.add(")");

        return builder.build();
    }

    /**
     * <pre>{@code
     *     <util:list id="myList" value-type="java.lang.String">
     *         ...
     *     </util:list>
     * }</code>
     * becomes this {@code CodeBlock}:
     * <pre>{@code
     *      List<String> myList = new ArrayList<>()
     *  }</code>
     */
    @Deprecated
    public CodeBlock createListBeanConstructorCall(Object listType, String beanVariableName) {
        return CodeBlock.of("List<$T> $L = new ArrayList<>()", listType, beanVariableName);
    }
}
