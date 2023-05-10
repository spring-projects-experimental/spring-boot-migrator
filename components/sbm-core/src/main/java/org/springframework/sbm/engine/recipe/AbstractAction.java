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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAction implements Action {

    @NotBlank
    private String description;

    private Condition condition = Condition.TRUE;

    @Autowired
    @JsonIgnore
    @Getter
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String getDetailedDescription() {
        final String conditionDescription =
                (getCondition().getDescription() == null || getCondition().getDescription().isBlank())
                        ? ""
                        : getCondition().getDescription() + " then\n\t  ";
        return conditionDescription + getDescription();
    }

    @Override
    public void applyInternal(ProjectContext context) {
        apply(context);

    }

    @Override
    public boolean isAutomated() {
        return !this.getClass().isAssignableFrom(DisplayDescription.class);
    }
}
