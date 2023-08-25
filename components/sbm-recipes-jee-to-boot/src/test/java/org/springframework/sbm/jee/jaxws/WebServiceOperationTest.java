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
package org.springframework.sbm.jee.jaxws;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Boyko
 */
public class WebServiceOperationTest {

    @Test
    void simple() {
        WebServiceOperation.Type input = new WebServiceOperation.Type();
        input.setSimpleName("Sum");
        input.setPackageName("org.superbiz.wsdl");
        input.setFields(new String[]{"p", "arg1"});

        WebServiceOperation.Type output = new WebServiceOperation.Type();
        output.setSimpleName("SumResponse");
        output.setPackageName("org.superbiz.wsdl");
        output.setFields(new String[]{"return"});

        WebServiceOperation op = new WebServiceOperation();
        op.setName("sum");
        op.setServiceMethodName("add");
        op.setInput(input);
        op.setOutput(output);

        assertThat(op.getImports()).containsExactly(
                "org.springframework.ws.server.endpoint.annotation.PayloadRoot",
                "org.springframework.ws.server.endpoint.annotation.RequestPayload",
                "org.springframework.ws.server.endpoint.annotation.ResponsePayload",
                "org.superbiz.wsdl.Sum",
                "org.superbiz.wsdl.SumResponse"
        );

        assertThat(op.createSnippet("calculator", "NAMESPACE_URI")).isEqualTo("" +
                "@PayloadRoot(namespace = NAMESPACE_URI, localPart = \"sum\")\n" +
                "@ResponsePayload\n" +
                "public SumResponse sum(@RequestPayload Sum request) {\n" +
                "\tSumResponse response = new SumResponse();\n" +
                "\tresponse.setReturn(calculator.add(request.getP(), request.getArg1()));\n" +
                "\treturn response;\n" +
                "}\n"
        );
    }

    @Test
    void oneWay() {
        WebServiceOperation.Type input = new WebServiceOperation.Type();
        input.setSimpleName("Sum");
        input.setPackageName("org.superbiz.wsdl");
        input.setFields(new String[]{"p", "arg1"});

        WebServiceOperation op = new WebServiceOperation();
        op.setName("sum");
        op.setServiceMethodName("add");
        op.setInput(input);

        assertThat(op.getImports()).containsExactly(
                "org.springframework.ws.server.endpoint.annotation.PayloadRoot",
                "org.springframework.ws.server.endpoint.annotation.RequestPayload",
                "org.superbiz.wsdl.Sum"
        );

        assertThat(op.createSnippet("calculator", "NAMESPACE_URI")).isEqualTo("" +
                "@PayloadRoot(namespace = NAMESPACE_URI, localPart = \"sum\")\n" +
                "public void sum(@RequestPayload Sum request) {\n" +
                "\tcalculator.add(request.getP(), request.getArg1());\n" +
                "}\n"
        );
    }
}
