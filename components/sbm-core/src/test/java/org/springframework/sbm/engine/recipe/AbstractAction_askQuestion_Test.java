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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.events.UserInputRequestedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Fabian Krüger
 */
//@SpringBootTest(classes={AbstractAction_askQuestion_Test.MyAction.class, ApplicationContextRunner.class})
//@RecordApplicationEvents
class AbstractAction_askQuestion_Test {

    @Component
    public static class MyAction extends AbstractAction {

        @Override
        public void apply(ProjectContext context) {
            Answer answer = askQuestion(
                    SelectOneQuestion.builder()
                            .option(Option.of("A1", "A"))
                            .option(Option.of("B2", "B"))
                            .build()
            );
        }
    }

//    @Autowired
//    MyAction myAction;
//
//    @Autowired
//    ApplicationEventPublisher eventPublisher;
//
//    @Autowired
//    private ApplicationEvents applicationEvents;

    @Test
    void test_renameMe() {

        new ApplicationContextRunner()
                .withBean(UserInputRequestedEventListener.class)
                .withBean(MyAction.class)
                .run(ctx -> {
                    MyAction myAction = ctx.getBean(MyAction.class);
                    SelectOneQuestion question = SelectOneQuestion.builder()
                            .text("select one")
                            .option(Option.of("A", "A"))
                            .option(Option.of("B", "B"))
                            .build();
                    Answer answer = myAction.askQuestion(question);
                    assertThat(answer).isNotNull();
                    System.out.println(answer.userInput());
                });
    }

    @Component
    @RequiredArgsConstructor
    public static class UserInputRequestedEventListener implements ApplicationListener<UserInputRequestedEvent> {
        private final ApplicationEventPublisher eventPublisher;

        @Override
        public void onApplicationEvent(UserInputRequestedEvent event) {
            SelectOneQuestion source = (SelectOneQuestion) event.getSource();
            eventPublisher.publishEvent(new UserInputProvidedEvent(new Answer(List.of(source.getOptions().get(1).getKey()))));
        }
    }

}

