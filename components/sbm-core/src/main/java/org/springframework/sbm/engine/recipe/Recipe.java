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
package org.springframework.sbm.engine.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.sbm.engine.context.ProjectContext;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private String name;

    private String description;

    private Condition condition = Condition.TRUE;

    @JsonProperty(value = "actions", required = true)
    @Valid
    @NotEmpty
    @Singular
    private List<@ActionMustIncludeConditionConstraint Action> actions;

    private Integer order = 0;

    @JsonCreator
    public Recipe(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "actions", required = true) List<Action> actions,
            @JsonProperty(value = "condition") Condition condition,
            @JsonProperty(value = "order") Integer order
    ) {
        this.name = name;
        this.actions = actions;
        this.condition = condition == null ? Condition.TRUE : condition;
        this.order = order == null ? 0 : order;
    }

    public Recipe(
            String name,
            List<Action> actions,
            Condition condition
    ) {
        this.name = name;
        this.actions = actions;
        this.condition = condition == null ? Condition.TRUE : condition;
        this.order = 0;
    }

    public Recipe(
            String name,
            String description,
            Condition condition,
            List<Action> actions
    ) {
        this.name = name;
        this.description = description;
        this.actions = actions;
        this.condition = condition == null ? Condition.TRUE : condition;
    }


    public Recipe(String name, List<Action> actions) {
        this(name, actions, Condition.TRUE);
    }

    public boolean isApplicable(ProjectContext context) {
        return condition.evaluate(context) && actions.stream().anyMatch(a -> a.isApplicable(context));
    }

    public List<Action> apply(ProjectContext context) {

        List<Action> appliedActions = new ArrayList<>();
        for (Action action : actions) {

             if (action.isApplicable(context)) {
                 action.applyWithStatusEvent(context);
                 appliedActions.add(action);
             }
        }

        return appliedActions;
    }

    public String getDetails() {
        // FIXME: prints all actions, even so they were not applicable because the condition was false
        String conditionDescriptions = actions.stream()
                .map(a -> a.getCondition())
                .map(c -> "\t* " + c.getDescription() + "\n")
                .collect(Collectors.joining());

        String actionDescriptions = actions.stream()
                .map(a -> "\t* " + a.getDetailedDescription() + "\n")
                .collect(Collectors.joining());

        return this
                + "\nThis recipe is applicable to an app if:\n"
                + conditionDescriptions
                + "The recipe requires these actions to be performed\n"
                + actionDescriptions;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String prefix) {
        return new StringBuilder()
                .append(prefix == null ? "" : prefix)
                .append(name)
                .append(" - ")
                .append(null == description ? "<no description>" : description)
                .toString();
    }

    public RecipeAutomation getAutomationInfo() {
        long numAutomated = getActions().stream()
                .filter(a -> true == a.isAutomated())
                .count();

        RecipeAutomation automationInfo = numAutomated ==
                getActions().size() ? RecipeAutomation.AUTOMATED :
                numAutomated == 0 ? RecipeAutomation.MANUAL : RecipeAutomation.PARTIALLY_AUTOMATED;
        return automationInfo;
    }


}
