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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.events.ActionFailedEvent;
import org.springframework.sbm.engine.events.ActionStartedEvent;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ActionTest {

    @Spy
    private ApplicationEventPublisher publisher;

    @Mock
    ProjectContext projectContext;

    @InjectMocks
    private TestActionImpl testAction;

    static class TestActionImpl implements Action {
        private final ApplicationEventPublisher publisher;

        TestActionImpl(ApplicationEventPublisher publisher) {
            this.publisher = publisher;
        }

        @Override
        public String getDescription() {
            return "Test failing action";
        }

        @Override
        public String getDetailedDescription() {
            return null;
        }

        @Override
        public Condition getCondition() {
            return null;
        }

        @Override
        public void apply(ProjectContext context) {
            throw new RuntimeException("my exception");
        }

        @Override
        public void applyInternal(ProjectContext context) {
            apply(context);
        }

        @Override
        public ApplicationEventPublisher getEventPublisher() {
            return publisher;
        }

        @Override
        public boolean isAutomated() {
            return false;
        }
    }

    @Test
    void applyWithStatusEvent() {
        assertThrows(RuntimeException.class, () -> testAction.applyWithStatusEvent(projectContext));

        Mockito.verify(publisher).publishEvent(any(ActionStartedEvent.class));
        Mockito.verify(publisher).publishEvent(any(ActionFailedEvent.class));
    }
}
