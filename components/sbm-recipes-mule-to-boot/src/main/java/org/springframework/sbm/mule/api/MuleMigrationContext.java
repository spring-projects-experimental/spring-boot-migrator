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
package org.springframework.sbm.mule.api;

import org.mulesoft.schema.mule.core.AnnotatedType;
import org.springframework.sbm.properties.api.PropertiesSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.core.SubFlowType;
import org.mulesoft.schema.mule.tls.TlsContextType;

import javax.xml.bind.JAXBElement;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MuleMigrationContext {
    private List<JAXBElement> availableFlows;
    private List<SubFlowType> availableMuleSubFlows;
    private final MuleConfigurations muleConfigurations;
    private final List<PropertiesSource> propertiesFiles;
    private final List<JAXBElement> nonSupportedTypes;

    public MuleMigrationContext(
            List<JAXBElement> availableFlows,
            List<SubFlowType> availableMuleSubFlows,
            MuleConfigurations muleConfigurations,
            List<PropertiesSource> propertiesFiles,
            List<JAXBElement> notSupportedTypes
    ) {
        this.availableFlows = availableFlows;
        this.availableMuleSubFlows = availableMuleSubFlows;
        this.muleConfigurations = muleConfigurations;
        this.propertiesFiles = propertiesFiles;
        this.nonSupportedTypes = notSupportedTypes;
    }
}
