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

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.mule.actions.javadsl.MuleToJavaDSLTranslator;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import lombok.Builder;

import java.nio.file.Path;
import java.util.List;

@Builder
public class JavaDSLAction extends AbstractAction {
    @Override
    public void apply(ProjectContext projectContext) {
        List<MuleXml> muleSearch = projectContext.search(new MuleXmlProjectResourceFilter());

        muleSearch.forEach(mx -> {
            MuleXml muleXml = mx;
            MuleToJavaDSLTranslator muleToJavaDSLTranslator = new MuleToJavaDSLTranslator();
            String dslStatements = muleToJavaDSLTranslator.translate(muleXml.getXmlDocument());
            /**
             * TODO:
             *  * hardcoded package
             * */

            if (!dslStatements.contains("Http.inboundChannelAdapter")) {
                String javaCode = "package com.example.javadsl;\n" +
                        "\n" +
                        "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
                        "import org.springframework.amqp.rabbit.core.RabbitTemplate;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.amqp.dsl.Amqp;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class JavaDSLAmqp {\n" +
                        "    @Bean\n" +
                        "    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {\n" +
                        "        return --JAVA_DSL--\n" +
                        ".get();\n" +
                        "    }\n" +
                        "}";
                String preparedCode = javaCode.replaceAll("--JAVA_DSL--", dslStatements);
                projectContext.getModules().get(0).getMainJavaSourceSet().addJavaSource(projectContext.getProjectRootDirectory(), preparedCode, "com.example.javadsl");

                String amqpConfigCode =
                        "package com.example.javadsl.config;\n" +
                                "\n" +
                                "import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;\n" +
                                "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
                                "import org.springframework.beans.factory.annotation.Value;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class AMQPConfig {\n" +
                                "\n" +
                                "    @Value(\"${AMQP_HOST:amqpHost}\")\n" +
                                "    private String amqpHost;\n" +
                                "\n" +
                                "    @Value(\"${AMQP_PORT:5672}\")\n" +
                                "    private Integer amqpPort;\n" +
                                "\n" +
                                "    @Bean\n" +
                                "    public ConnectionFactory connectionFactory() {\n" +
                                "\n" +
                                "        return new CachingConnectionFactory(amqpHost, amqpPort);\n" +
                                "    }\n" +
                                "}\n";
                projectContext.getModules().get(0).getMainJavaSourceSet().addJavaSource(projectContext.getProjectRootDirectory(), Path.of("src/main/java"), amqpConfigCode);
            } else {
                String httpConfigCode =
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
                                "import org.springframework.amqp.rabbit.core.RabbitTemplate;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.http.dsl.Http;\n" +
                                "import org.springframework.integration.transformer.ObjectToStringTransformer;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class JavaDSLHttp {\n" +
                                "    @Bean\n" +
                                "    public IntegrationFlow inbound() {\n" +
                                "        return IntegrationFlows.from(\n" +
                                "                        Http.inboundChannelAdapter(\"/test\"))\n" +
                                "                        .transform(new ObjectToStringTransformer())\n" +
                                ".log()\n" +
                                ".get();\n" +
                                "    }\n" +
                                "}";
                projectContext.getModules().get(0).getMainJavaSourceSet().addJavaSource(projectContext.getProjectRootDirectory(), Path.of("src/main/java"), httpConfigCode);
            }
        });
    }
}
