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
package org.springframework.sbm.shell2.shell;

import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.shell.component.MultiItemSelector;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@ShellComponent
public class UserInputRequester extends AbstractShellComponent {
    public Answer ask(Question question) {
        if(SelectOneQuestion.class.isInstance(question)) {
            SelectOneQuestion selectOneQuestion = SelectOneQuestion.class.cast(question);
            return handleSelectOneQuestion(selectOneQuestion);
        }
        else if(SelectMultipleQuestion.class.isInstance(question)) {
            SelectMultipleQuestion selectMultipleQuestion = SelectMultipleQuestion.class.cast(question);
            return handleSelectMultipleQuestion(selectMultipleQuestion);
        }
        return null;
    }

    private Answer handleSelectMultipleQuestion(SelectMultipleQuestion selectMultipleQuestion) {
        return null;
    }

    @NotNull
    @ShellMethod
    private Answer handleSelectOneQuestion(SelectOneQuestion question) {
        List<Option> questionOptions = question.getOptions();
        List<SelectorItem<String>> options = mapToOptions(questionOptions);
        SingleItemSelector<String, SelectorItem<String>> component = new SingleItemSelector<>(getTerminal(), options, question.getText(), null);
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelector.SingleItemSelectorContext<String, SelectorItem<String>> context = component
                .run(SingleItemSelector.SingleItemSelectorContext.empty());
        String result = context.getResultItems().stream()
                .map(si -> si.getItem())
                .collect(Collectors.joining(","));

        System.out.println("Got value " + result);

        return new Answer(result);
    }

    @NotNull
    private List<SelectorItem<String>> mapToOptions(List<Option> questionOptions) {
        List<SelectorItem<String>> options = questionOptions
                .stream()
                .map(o -> SelectorItem.of(o.getKey(), o.getValue()))
                .collect(Collectors.toList());
        return options;
    }


    private Answer askSelectMultipleQuestion(Question question, SelectMultipleQuestion selectMultipleQuestion) {
        return null;
    }

    @NotNull
    private Answer askSelectOneQuestion(Question question, SelectOneQuestion selectOneQuestion) {
        Answer answer;
        List<SelectorItem<String>> options = mapToOptions(selectOneQuestion.getOptions());

//            List<SelectorItem<String>> items = scanResult.applicableRecipes().stream().map(r -> SelectorItem.of(r.getRecipeName(), r.getRecipeName())).collect(
//                    Collectors.toList());
//            items.add(SelectorItem.of("key1", "value1"));
//            items.add(SelectorItem.of("key2", "value2", false, true));
//            items.add(SelectorItem.of("key3", "value3"));
        MultiItemSelector<String, SelectorItem<String>> component = new MultiItemSelector<>(getTerminal(),
                                                                                            options, question.getText(), null);
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        MultiItemSelector.MultiItemSelectorContext<String, SelectorItem<String>> context = component
                .run(MultiItemSelector.MultiItemSelectorContext.empty());
        String result = context.getResultItems().stream()
                .map(si -> si.getItem())
                .collect(Collectors.joining(","));
        System.out.println("Got value " + result);
        answer = new Answer(selectOneQuestion.getOptions().get(0).getKey());
        return answer;
    }
}
