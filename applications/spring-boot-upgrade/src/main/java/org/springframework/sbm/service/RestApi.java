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
package org.springframework.sbm.service;

import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.service.dto.RecipeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {
    
    @Autowired
    private ScanCommand scanCommand;
    
    @Autowired
    private ApplyCommand applyCommand;
    
    @Autowired
    private ApplicableRecipeListCommand applicableRecipeListCommand;

    @PostMapping(value = "/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    RecipeInfo[] scan(@RequestParam("projectPath") String projectPath) {
        ProjectContext projectContext = scanCommand.execute(projectPath);
        return applicableRecipeListCommand.execute(projectContext).stream()
                    .map(r -> RecipeInfo.builder().name(r.getName()).description(r.getDescription()).details(r.getDetails()).build())
                    .toArray(RecipeInfo[]::new);
    }
    
    @PostMapping("/apply")
    void apply(@RequestParam("projectPath") String projectPath, @RequestParam("recipe") String recipe) {
        // FIXME: ProjectContext must be cached and used here
        // FIXME: Apply should not do implicit scan.
        ProjectContext projectContext = scanCommand.execute(projectPath);
        applyCommand.execute(projectContext, recipe);
    }

}
