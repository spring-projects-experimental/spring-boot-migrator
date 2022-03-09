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
import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;

@Component
public class DwlTransformTranslator implements MuleComponentToSpringIntegrationDslTranslator<TransformMessageType> {
    public static final String STATEMENT_CONTENT = ".transform($CLASSNAME::transform)";
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
    public DslSnippet translate(
            TransformMessageType component,
            QName name,
            MuleConfigurations muleConfigurations,
            String flowName
    ) {

        if (isComponentReferencingAnExternalFile(component)) {
            return formExternalFileBasedDSLSnippet(component);
        }

        return formEmbeddedDWLBasedDSLSnippet(component, Helper.sanitizeForBeanMethodName(flowName));
    }

    private DslSnippet formEmbeddedDWLBasedDSLSnippet(TransformMessageType component, String flowName) {
        String className = capitalizeFirstLetter(flowName) + "ActionTransform";

        String dwlContent = component.getSetPayload().getContent().toString();
        String dwlContentCommented = "     * " + dwlContent.replace("\n", "\n     * ") + "\n";
        String externalClassContent =
                replaceClassName(externalClassContentPrefixTemplate, className) +
                        dwlContentCommented +
                        replaceClassName(externalClassContentSuffixTemplate, className);
        return new DslSnippet(replaceClassName(STATEMENT_CONTENT, className), Collections.emptySet(), Collections.emptySet(), externalClassContent);
    }

    private DslSnippet formExternalFileBasedDSLSnippet(TransformMessageType component) {
        String resource = component.getSetPayload().getResource();
        String className = capitalizeFirstLetter(getFileName(resource)) + "ActionTransform";
        String content =
                replaceClassName(externalClassContentPrefixTemplate, className)
                        + "     * from file "
                        + resource.replace("classpath:", "")
                        + replaceClassName(externalClassContentSuffixTemplate, className);
        return new DslSnippet(replaceClassName(STATEMENT_CONTENT, className), Collections.emptySet(), Collections.emptySet(), content);
    }

    private boolean isComponentReferencingAnExternalFile(TransformMessageType component) {
        return component.getSetPayload().getContent().isEmpty();
    }

    private String getFileName(String path) {

        String[] fileParts = path.split("\\.");
        String pathWithoutExtension = fileParts[fileParts.length - 2];
        String[] fileNameParts = pathWithoutExtension.split("/");
        return fileNameParts[fileNameParts.length - 1];
    }


    private String replaceClassName(String template, String className) {
        return template.replace("$CLASSNAME", className);
    }

    private String capitalizeFirstLetter(String className) {
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }
}
