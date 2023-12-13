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
package org.springframework.rewrite.scopes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.lang.Nullable;
import org.springframework.rewrite.parsers.RewriteExecutionContextErrorHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fabian Krüger
 */
public class AbstractBaseScope implements Scope {

	private static final Logger LOGGER = LoggerFactory.getLogger(RewriteExecutionContextErrorHandler.class);

	private final Map<String, Object> scopedBeans = new ConcurrentHashMap<>();

	public void clear(ConfigurableListableBeanFactory beanFactory) {
		LOGGER.trace(
				"Clearing %d beans from scope %s.".formatted(scopedBeans.keySet().size(), this.getClass().getName()));
		scopedBeans.keySet().stream().forEach(beanName -> {
			beanFactory.destroyScopedBean(beanName);
			LOGGER.trace("Removed bean '%s' from scan scope.".formatted(beanName));
		});
	}

	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object scopedObject = this.scopedBeans.get(name);
		if (scopedObject == null) {
			scopedObject = objectFactory.getObject();
			this.scopedBeans.put(name, scopedObject);
		}
		return scopedObject;
	}

	@Nullable
	public Object remove(String name) {
		Map<String, Object> scope = this.scopedBeans;
		return scope.remove(name);
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		LOGGER.warn("%s does not support destruction callbacks.".formatted(this.getClass().getName()));
	}

	@Nullable
	public Object resolveContextualObject(String key) {
		return null;
	}

	public String getConversationId() {
		return null;
	}

}
