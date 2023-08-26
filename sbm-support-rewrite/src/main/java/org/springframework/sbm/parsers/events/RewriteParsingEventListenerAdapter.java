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
package org.springframework.sbm.parsers.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.tree.ParsingEventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adapter listening to OpenRewrite ParsingEvents and publishing them as Spring application events.
 *
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewriteParsingEventListenerAdapter implements ParsingEventListener {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void parsed(Parser.Input input, SourceFile sourceFile) {
        log.debug("Parsed %s to %s".formatted(input.getPath(), sourceFile.getSourcePath()));
        eventPublisher.publishEvent(new ParsedResourceEvent(input, sourceFile));
    }
}
