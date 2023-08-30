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
package org.springframework.sbm.mule.api.toplevel;

import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;

public class FlowTopLevelElement extends AbstractTopLevelElement {

    public FlowTopLevelElement(String flowName,
                               List<JAXBElement<?>> elements,
                               MuleConfigurations muleConfigurations,
                               Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap)  {
        super(flowName, elements, muleConfigurations, translatorsMap);
    }

    protected String composeSuffixDslCode() {
        return ".get();\n";
    }
}
