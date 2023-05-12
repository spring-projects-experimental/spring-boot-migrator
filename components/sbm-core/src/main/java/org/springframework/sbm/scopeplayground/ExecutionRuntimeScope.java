
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fabian Krüger
 */
@Component
public class ExecutionRuntimeScope implements Scope /*extends SimpleThreadScope*/ {

    public final static String SCOPE_NAME = "executionScope";
    private final static String TARGET_NAME_PREFIX = SCOPE_NAME + ".";

    public void clear(ConfigurableListableBeanFactory beanFactory) {
        for(String beanName : beanFactory.getBeanDefinitionNames()) {
            if(threadScope.keySet().contains(beanName)) {
                beanFactory.destroyScopedBean(beanName);
            }
        }
        threadScope.clear();
    }

    public String getTargetNamePrefix() {
        return TARGET_NAME_PREFIX;
    }


    private static final Log logger = LogFactory.getLog(SimpleThreadScope.class);
    private final Map<String, Object> threadScope = new ConcurrentHashMap<>();

    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = this.threadScope;
        Object scopedObject = scope.get(name);
        if (scopedObject == null) {
            scopedObject = objectFactory.getObject();
            scope.put(name, scopedObject);
        }
        return scopedObject;
    }

    @Nullable
    public Object remove(String name) {
        Map<String, Object> scope = this.threadScope;
        return scope.remove(name);
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        logger.warn("SimpleThreadScope does not support destruction callbacks. Consider using RequestScope in a web environment.");
    }

    @Nullable
    public Object resolveContextualObject(String key) {
        return null;
    }

    public String getConversationId() {
        return Thread.currentThread().getName();
    }
}
