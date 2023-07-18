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
package org.springframework.sbm.engine.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ActionDeserializerRegistry {
    private final List<ActionDeserializer> deserializers;

    @Deprecated
    public void register(Class<? extends Action> key, ActionDeserializer actionDeserializationDispatcher) {
        this.deserializers.add(actionDeserializationDispatcher);
    }

    public ActionDeserializer get(Class<? extends Action> key) {
        return deserializers.stream()
                .filter(d -> d.canHandle(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No deserializer found for '" + key + "'"));
    }
}
