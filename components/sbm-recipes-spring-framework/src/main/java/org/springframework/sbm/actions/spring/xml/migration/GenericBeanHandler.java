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
import com.squareup.javapoet.MethodSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;

@Component
@RequiredArgsConstructor
public class GenericBeanHandler implements BeanHandler {

    private final Helper helper;
    private final BeanPropertySetterStatementFactory beanPropertySetterStatementFactory;
    private final BeanConstructorCallFactory beanConstructorCallFactory;

    @Override
    public MethodSpec createBeanDefinitionMethod(MigrationContext migrationContext, XmlBeanDef xmlBeanDefinition) throws ClassNotFoundException {

        String methodName = Helper.calculateMethodName(xmlBeanDefinition.getName());

        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Bean.class)
                .addModifiers(Modifier.PUBLIC);

        String beanClassName = xmlBeanDefinition.getBeanDefinition().getBeanClassName();
        String beanVariableName = Helper.calculateVariableName(xmlBeanDefinition);// firstToLower(beanClassName);

        CodeBlock constructorCallStatement = beanConstructorCallFactory.createConstructorCall(migrationContext, beanVariableName, xmlBeanDefinition.getBeanDefinition(), beanClassName);
        CodeBlock beanInitStatements = beanPropertySetterStatementFactory.createSetterCalls(migrationContext, beanVariableName, xmlBeanDefinition.getBeanDefinition(), xmlBeanDefinition);

        CodeBlock methodBody =
                CodeBlock.builder()
                        .addStatement(constructorCallStatement)
                        .add(beanInitStatements)
                        .addStatement(CodeBlock.of("return " + beanVariableName))
                        .build();

        Class<?> returnType = migrationContext.getClass(xmlBeanDefinition.getBeanDefinition().getBeanClassName());
//        Class<?> returnType = Class.forName(beanDefinition.getBeanDefinition().getBeanClassName());

        methodSpec
                .returns(returnType)
                .addCode(methodBody);

        return methodSpec.build();
    }
}
