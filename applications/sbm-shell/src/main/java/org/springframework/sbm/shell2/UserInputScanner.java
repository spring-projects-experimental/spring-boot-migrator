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

import org.springframework.shell.component.PathInput;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@Component
public class UserInputScanner extends AbstractShellComponent {
    public Path askForPath() {
        PathInput component = new PathInput(getTerminal(), "Enter value");
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        PathInput.PathInputContext context = component.run(PathInput.PathInputContext.empty());
        Path projectRoot = context.getResultValue();
        return projectRoot;
    }

    public String askForSingleSelection(List<SelectorItem<String>> items) {

        SingleItemSelector<String, SelectorItem<String>> component = new SingleItemSelector<>(getTerminal(), items, "applicableRecipes", null);

        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelector.SingleItemSelectorContext<String, SelectorItem<String>> context = component.run(SingleItemSelector.SingleItemSelectorContext.empty());

        return context.getResultItem().get().getItem();
    }
}
