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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;

@Component
@RequiredArgsConstructor
public class ListBeanHandler implements BeanHandler {

    private final Helper helper;
    private final BeanConstructorCallFactory beanConstructorCallFactory;
    private final BeanPropertySetterStatementFactory beanPropertySetterStatementFactory;

    public MethodSpec createBeanDefinitionMethod(MigrationContext migrationContext, XmlBeanDef xmlBeanDefinition) throws ClassNotFoundException {

        String methodName = Helper.calculateMethodName(xmlBeanDefinition.getName());

        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Bean.class)
                .addModifiers(Modifier.PUBLIC);

        String beanVariableName = xmlBeanDefinition.getName();// firstToLower(beanClassName);

        String elementTypeName = ((ManagedList) xmlBeanDefinition.getBeanDefinition().getPropertyValues().getPropertyValues()[0].getValue()).getElementTypeName();
        Class<?> listType = migrationContext.getClass(elementTypeName);

        CodeBlock newBeanStatement = beanConstructorCallFactory.createListBeanConstructorCall(listType, beanVariableName);
        CodeBlock beanInitStatements = beanPropertySetterStatementFactory.createListBeanSetterCalls(beanVariableName, xmlBeanDefinition);

        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get("java.util", "List"), TypeName.get(listType)
        );

        CodeBlock methodBody = CodeBlock.builder()
                .addStatement(newBeanStatement)
                .add(beanInitStatements)
                .addStatement(CodeBlock.of("return " + beanVariableName))
                .build();

        methodSpec
                .returns(returnType)
                .addCode(methodBody);
        return methodSpec.build();
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
    public CodeBlock createListBeanConstructorCall(Object listType, String beanVariableName) {
        return CodeBlock.of("List<$T> $L = new ArrayList<>()", listType, beanVariableName);
    }

}
