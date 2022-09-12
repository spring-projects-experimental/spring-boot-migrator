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
package org.springframework.sbm.shell2.client.api;

import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.shell2.ScanProgressUpdate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Fabian Krüger
 */
@Component
public class SbmClientFactory {
    public SbmClient create(Consumer<ScanProgressUpdate> scanProgressUpdateConsumer,
                            Consumer<ScanResult> scanResultConsumer,
                            Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer,
                            Consumer<RecipeExecutionResult> recipeExecutionResultConsumer,
                            Function<Question, Answer> questionConsumer) {

        SbmClient.SbmClientStompClientSessionHandler stompSessionHandler = new SbmClient.SbmClientStompClientSessionHandler(
                scanProgressUpdateConsumer,
                scanResultConsumer,
                recipeExecutionProgressConsumer,
                recipeExecutionResultConsumer,
                questionConsumer);

        SbmClient sbmClient = new SbmClient(stompSessionHandler);

        return sbmClient;
    }
}
