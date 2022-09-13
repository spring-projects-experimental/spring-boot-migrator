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

import lombok.experimental.SuperBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.engine.context.ProjectContext;

/**
 * @author Fabian Krüger
 */
@Configuration
public class UserInputRecipeDefinition {
    @Bean
    public Recipe userInputRecipe(ApplicationEventPublisher eventPublisher) {
        MyAction action = MyAction.builder().condition(Condition.TRUE).build();
        action.setEventPublisher(eventPublisher);
        return Recipe.builder()
                .name("my-test")
                .condition(Condition.TRUE)
                .action(action)
                .build();
    }

    @SuperBuilder
    public static class MyAction extends AbstractAction{
        @Override
        public void apply(ProjectContext context) {
            askQuestion(SelectOneQuestion.builder()
                                .option(Option.of("1", "One"))
                                .option(Option.of("2", "Two"))
                                .build()
            );
        }
    }
}
