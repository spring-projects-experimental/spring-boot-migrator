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
package org.springframework.sbm.boot.upgrade_27_30;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.precondition.PreconditionCheck;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sbu30_PreconditionChecks extends AbstractAction {

    @Autowired
    @JsonIgnore
    private List<Sbu30_PreconditionCheck> checks = new ArrayList<>();

    @Override
    public void apply(ProjectContext context) {
        List<Sbu30_PreconditionCheckResult> results = checks.stream()
                .map(sbu30_preconditionCheck -> sbu30_preconditionCheck.run(context))
                .collect(Collectors.toList());

        results.forEach(this::render);

        if(results.stream().anyMatch(c -> c.getState().equals(PreconditionCheck.ResultState.FAILED))) {
            // TODO: ask user to proceed, fail/end otherwise
        }

    }

    private void render(Sbu30_PreconditionCheckResult result) {
        System.out.println(result.getMessage());
    }
}
