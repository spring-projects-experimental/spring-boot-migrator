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
package org.springframework.sbm.shell2;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@ShellComponent
@RequiredArgsConstructor
public class UserInputRequester extends AbstractShellComponent {

    private final UserInputScanner userInputScanner;

    public Answer ask(Question question) {
        if(SelectOneQuestion.class.isInstance(question)) {
            SelectOneQuestion selectOneQuestion = SelectOneQuestion.class.cast(question);
            List<SelectorItem<String>> selectorItems = getSelectorItems(selectOneQuestion.getOptions());
            String selection = userInputScanner.askForSingleSelection(selectorItems);
            return new Answer(List.of(selection));
        }
        else if(SelectMultipleQuestion.class.isInstance(question)) {
            SelectMultipleQuestion selectMultipleQuestion = SelectMultipleQuestion.class.cast(question);
            List<SelectorItem<String>> selectorItems = getSelectorItems(selectMultipleQuestion.getOptions());
            List<String> selection = userInputScanner.askForMultipleSelection(selectorItems);
            return new Answer(selection);
        }
        else if(PathQuestion.class.isInstance(question)) {
            PathQuestion pathQuestion = PathQuestion.class.cast(question);
            Path selection = userInputScanner.askForPath(pathQuestion.getText());
            List<String> userInput = List.of(selection.toString());
            return new Answer(userInput);
        }
        return null;
    }

    @NotNull
    private List<SelectorItem<String>> getSelectorItems(List<Option> selectOneQuestion) {
        List<SelectorItem<String>> selectorItems = selectOneQuestion
                .stream()
                .map(r -> SelectorItem.of(r.getKey(), r.getValue()))
                .collect(Collectors.toList());
        return selectorItems;
    }

}
