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
package org.springframework.sbm.scopeplayground;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.stereotype.Component;

/**
 * @author Fabian Krüger
 */
@Component
public class ScanRuntimeScope extends SimpleThreadScope {

    public final static String SCOPE_NAME = "executionScope";
    private final static String TARGET_NAME_PREFIX = SCOPE_NAME + ".";

    public void clear(ConfigurableListableBeanFactory beanFactory) {
        for(String beanName : beanFactory.getBeanDefinitionNames()) {
            if(beanName.startsWith(getTargetNamePrefix())) {
                beanFactory.destroyScopedBean(beanName);
            }
        }
    }

    public String getTargetNamePrefix() {
        return TARGET_NAME_PREFIX;
    }
}
