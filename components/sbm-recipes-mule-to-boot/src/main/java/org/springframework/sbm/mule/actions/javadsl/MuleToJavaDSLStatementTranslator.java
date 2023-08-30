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
package org.springframework.sbm.mule.actions.javadsl;

import org.w3c.dom.Element;

import javax.xml.xpath.*;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class MuleToJavaDSLStatementTranslator {

    private final String template;
    private final String propertyName;
    private final String xPathAttr;

    public MuleToJavaDSLStatementTranslator(String template, String propertyName, String xPathAttr) {
        this.template = template;
        this.propertyName = propertyName;
        this.xPathAttr = xPathAttr;
    }

    public String translate(Element muleElement) {
        if (!isEmpty(propertyName ) && !isEmpty(xPathAttr)) {
            String value = getAttributeValue(muleElement);
            String attributePlaceholder = "--" + propertyName + "--";
            return this.template.replaceAll(attributePlaceholder, value);
        } else {
            return template;
        }
    }

    private String getAttributeValue(Element element) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathAttrExpression = xPath.compile(xPathAttr);
            return (String) xPathAttrExpression.evaluate(element, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Incorrect attribute xPath expression: " + e.getMessage(), e);
        }
    }
}
