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

package org.springframework.sbm.mule.actions.javadsl.translators.dwl;

import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;

@Component
public class DwlTransformTranslator implements MuleComponentToSpringIntegrationDslTranslator<TransformMessageType> {
    public static final String STATEMENT_CONTENT = ".transform(ActionTransform::transform)";
    private static final String externalClassContentPrefixTemplate = "package com.example.javadsl;\n" +
            "\n" +
            "public class $CLASSNAME {\n" +
            "    /*\n" +
            "     * TODO:\n" +
            "     *\n" +
            "     * Please add necessary transformation for below snippet\n";

    private static final String externalClassContentSuffixTemplate = "     * */\n" +
            "    public static $CLASSNAME transform(Object payload) {\n" +
            "\n" +
            "        return new $CLASSNAME();\n" +
            "    }\n" +
            "}";

    @Override
    public Class<TransformMessageType> getSupportedMuleType() {
        return TransformMessageType.class;
    }

    @Override
    public DslSnippet translate(TransformMessageType component, QName name, MuleConfigurations muleConfigurations) {

        String prefix = replaceClassName(externalClassContentPrefixTemplate, "ActionTransform");
        String suffix = replaceClassName(externalClassContentSuffixTemplate, "ActionTransform");

        if (component.getSetPayload().getContent().isEmpty()) {
            String content =
                    externalClassContentPrefixTemplate
                            + "     * from file "
                            + component.getSetPayload().getResource().replace("classpath:", "")
                            + externalClassContentSuffixTemplate;
            return new DslSnippet(STATEMENT_CONTENT, Collections.emptySet(), Collections.emptySet(), content);
        }

        String dwlContent = component.getSetPayload().getContent().toString();
        String dwlContentCommented = "     * " + dwlContent.replace("\n", "\n     * ") + "\n";
        String externalClassContent = prefix + dwlContentCommented + suffix;
        return new DslSnippet(STATEMENT_CONTENT, Collections.emptySet(), Collections.emptySet(), externalClassContent);
    }


    private String replaceClassName(String template, String className) {
        return template.replace("$CLASSNAME", className);
    }
}
