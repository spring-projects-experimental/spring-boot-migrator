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
package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnknownFlowTest extends JavaDSLActionBaseTest {

    private final static String muleMultiFlow = """
            <?xml version="1.0" encoding="UTF-8"?>
            <mule xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
            xmlns:spring="http://www.springframework.org/schema/beans"\s
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd">
            <catch-exception-strategy name="exceptionStrategy"/>
            </mule>
            """;

    @Test
    public void shouldTranslateUnknownFlow() {

        addXMLFileToResource(muleMultiFlow);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);

            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                        package com.example.javadsl;
                        import org.springframework.context.annotation.Configuration;
                        @Configuration
                        public class FlowConfigurations {
                            void catch_exception_strategy() {
                                //FIXME: element is not supported for conversion: <catch-exception-strategy/>
                            }
                        }"""
                    );
        });

    }
}
