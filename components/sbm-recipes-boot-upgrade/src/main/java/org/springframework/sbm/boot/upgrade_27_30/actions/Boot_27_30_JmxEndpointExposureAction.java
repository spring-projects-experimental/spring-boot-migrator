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
package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.properties.finder.SpringBootDefaultPropertiesFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.util.Optional;

public class Boot_27_30_JmxEndpointExposureAction extends AbstractAction {

    public static final String JMX_ENDPOINT = "management.endpoints.jmx.exposure.include";

    @Override
    public void apply(ProjectContext context) {
        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();

        context.getApplicationModules()
                .getTopmostApplicationModules()
                .stream()
                .map(m -> m.searchMainResources(springBootDefaultPropertiesFinder))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(p -> {
                    if (!p.getProperty(JMX_ENDPOINT).isPresent()) {
                        p.setProperty(JMX_ENDPOINT, "*");
                    }
                });
    }
}
