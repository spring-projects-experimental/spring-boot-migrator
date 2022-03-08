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
    public static final String STATEMENT_CONTENT = ".transform(ActionTransform::createActionTransformer)";
    private String externalClassContent = "class ActionTransform {\n" +
            "    /*\n" +
            "     * TODO:\n" +
            "     *\n" +
            "     * Please add necessary transformation for below snippet\n" +
            "     * %dw 1.0\n" +
            "     * %output application/json\n" +
            "     * ---\n" +
            "     * {\n" +
            "     *     action_Code: 10,\n" +
            "     *     returnCode:  20\n" +
            "     * }\n" +
            "     * */\n" +
            "    public static ActionTransform createActionTransformer(Object payload) {\n" +
            "\n" +
            "        return new ActionTransform();\n" +
            "    }\n" +
            "}\n";

    @Override
    public Class<TransformMessageType> getSupportedMuleType() {
        return TransformMessageType.class;
    }

    @Override
    public DslSnippet translate(TransformMessageType component, QName name, MuleConfigurations muleConfigurations) {
        return new DslSnippet(STATEMENT_CONTENT, Collections.emptySet(), Collections.emptySet(), externalClassContent);
    }
}
