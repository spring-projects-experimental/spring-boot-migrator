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
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.java.filter.FindJavaSourceContainingType;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateRamlToSpringMvcTest {

    @Test
    void test() {
        String raml =
                "#%RAML 1.0\n" +
                        "title: Remote Vending API\n" +
                        "version: v1.0\n" +
                        "baseUri: http://localhost/api\n" +
                        "mediaType: application/json\n" +
                        "documentation:\n" +
                        "  - title: Introduction\n" +
                        "    content: |\n" +
                        "      API to manage the sales and replenishments of stock items and floats in our vending machines.\n" +
                        "/sales:\n" +
                        "  post:\n" +
                        "    body:\n" +
                        "      example: |\n" +
                        "        {\n" +
                        "          \"machineId\" : \"ZX4102\",\n" +
                        "          \"trayId\" : \"A1\",\n" +
                        "          \"dateAndTime\" : \"2013-10-22 16:17:00\",\n" +
                        "          \"exchange\" : {\n" +
                        "            \"value\" : 450,\n" +
                        "            \"in\" : 500,\n" +
                        "            \"out\" : 50\n" +
                        "          }\n" +
                        "        }\n" +
                        "  get:\n" +
                        "    responses:\n" +
                        "      200:\n" +
                        "        body:\n" +
                        "          example: |\n" +
                        "            {\n" +
                        "                \"count\" : 2,\n" +
                        "                \"sales\" : [\n" +
                        "                  {\n" +
                        "                    \"dateAndTime\" : \"2013-10-22 16:17:00\",\n" +
                        "                    \"value\" : 450,\n" +
                        "                    \"machineId\" : \"ZX4102\",\n" +
                        "                    \"productId\" : \"Cad-CB1012\"\n" +
                        "                  },\n" +
                        "                  {\n" +
                        "                    \"dateAndTime\" : \"2013-10-22 16:17:00\",\n" +
                        "                    \"value\" : 150,\n" +
                        "                    \"machineId\" : \"ZX5322\",\n" +
                        "                    \"productId\" : \"CC-LB1\"\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"totalValue\" : 600\n" +
                        "            }\n";

        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("com.foo.bar");

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withProjectRoot(Path.of("./target/testcode/raml-to-jaxrs").toAbsolutePath())
                .withSbmApplicationProperties(sbmApplicationProperties)
                .withBuildFileHavingDependencies(
                        "javax.ws.rs:javax.ws.rs-api:2.1.1",
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
                        "org.springframework.boot:spring-boot-starter-web:2.4.2"
                )
                .addProjectResource("src/main/resources/api.raml", raml)
                .build();

        // generate JAX-RS from RAML

        new org.springframework.sbm.mule.actions.MigrateRamlToSpringMvc(new BasePackageCalculator(sbmApplicationProperties)).apply(context);

        Optional<JavaSourceAndType> sales = context.search(new FindJavaSourceContainingType("com.foo.bar.resource.Sales"));
        assertThat(sales.get().getJavaSource().print()).isEqualTo(
                "package com.foo.bar.resource;\n" +
                        "\n" +
                        "import com.foo.bar.support.ResponseDelegate;\n" +
                        "import org.springframework.web.bind.annotation.RequestBody;\n" +
                        "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                        "import org.springframework.web.bind.annotation.RestController;\n" +
                        "\n" +
                        "import javax.ws.rs.core.Response;\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(value = \"/sales\")\n" +
                        "public interface Sales {\n" +
                        "    @RequestMapping(consumes = \"application/json\", method = RequestMethod.POST)\n" +
                        "    void postSales(@RequestBody Object entity);\n" +
                        "\n" +
                        "    @RequestMapping(produces = \"application/json\", method = RequestMethod.GET)\n" +
                        "    GetSalesResponse getSales();\n" +
                        "\n" +
                        "  class GetSalesResponse extends ResponseDelegate {\n" +
                        "    private GetSalesResponse(Response response, Object entity) {\n" +
                        "      super(response, entity);\n" +
                        "    }\n" +
                        "\n" +
                        "    private GetSalesResponse(Response response) {\n" +
                        "      super(response);\n" +
                        "    }\n" +
                        "\n" +
                        "    public static GetSalesResponse respond200WithApplicationJson(Object entity) {\n" +
                        "      Response.ResponseBuilder responseBuilder = Response.status(200).header(\"Content-Type\", \"application/json\");\n" +
                        "      responseBuilder.entity(entity);\n" +
                        "      return new GetSalesResponse(responseBuilder.build(), entity);\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n"
        );

        Optional<JavaSourceAndType> responseDelegateSource = context.search(new FindJavaSourceContainingType("com.foo.bar.support.ResponseDelegate"));
        assertThat(responseDelegateSource.get().getType().getSimpleName()).isEqualTo("ResponseDelegate"); // code changed dynamically, sp only class name check
    }
}
