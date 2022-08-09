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
