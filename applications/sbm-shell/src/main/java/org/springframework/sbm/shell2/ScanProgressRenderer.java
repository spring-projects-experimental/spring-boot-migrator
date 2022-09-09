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

import org.springframework.shell.component.MultiItemSelector;
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
public class ScanProgressRenderer extends AbstractShellComponent {
    public void renderResult(ScanResult scanResult) {
        List<SelectorItem<String>> items = scanResult.applicableRecipes().stream()
                .map(r -> SelectorItem.of(r.getName(), r.getName()))
                .collect(Collectors.toList());

        MultiItemSelector<String, SelectorItem<String>> component = new MultiItemSelector<>(getTerminal(), items, "testSimple", null);

        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        MultiItemSelector.MultiItemSelectorContext<String, SelectorItem<String>> context = component.run(MultiItemSelector.MultiItemSelectorContext.empty());

        String result = context.getResultItems().stream()
                .map(si -> si.getItem())
                .collect(Collectors.joining(","));

        System.out.println("Got value " + result);
    }

    public void renderUpdate(ScanUpdate scanUpdate) {

    }

    public void startScan(Path path) {
        System.out.println("start scan " + path);
    }
}
