/*
 * Copyright 2021 - 2022 the original author or authors.
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

    private final static String muleMultiFlow = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\">\n" +
            "<catch-exception-strategy name=\"exceptionStrategy\"/>\n" +
            "</mule>";

    @Test
    public void shouldTranslateUnknownFlow() {

        addXMLFileToResource(muleMultiFlow);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);

        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    void catch_exception_strategy() {\n" +
                        "        //FIXME: element is not supported for conversion: <catch-exception-strategy/>\n" +
                        "    }}"
                );
    }
}
