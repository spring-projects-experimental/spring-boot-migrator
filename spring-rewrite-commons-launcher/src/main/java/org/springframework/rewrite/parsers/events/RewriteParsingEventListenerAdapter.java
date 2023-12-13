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
package org.springframework.rewrite.parsers.events;

import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.tree.ParsingEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Adapter listening to OpenRewrite ParsingEvents and publishing them as Spring
 * application events.
 *
 * @author Fabian Krüger
 */
public class RewriteParsingEventListenerAdapter implements ParsingEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RewriteParsingEventListenerAdapter.class);

	private final ApplicationEventPublisher eventPublisher;

	public RewriteParsingEventListenerAdapter(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void intermediateMessage(String stateMessage) {
		eventPublisher.publishEvent(new IntermediateParsingEvent(stateMessage));
	}

	@Override
	public void startedParsing(Parser.Input input) {
		eventPublisher.publishEvent(new StartedParsingResourceEvent(input));
	}

	@Override
	public void parsed(Parser.Input input, SourceFile sourceFile) {
		LOGGER.debug("Parsed %s to %s".formatted(input.getPath(), sourceFile.getSourcePath()));
		eventPublisher.publishEvent(new FinishedParsingResourceEvent(input, sourceFile));
	}

}
