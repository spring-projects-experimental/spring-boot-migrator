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
package org.springframework.sbm;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportRenderer;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
class ReportController{

    @Autowired
    private ApplyCommand applyCommand;
    @Autowired
    private ReportHolder reportHolder;

    @Autowired
    private ProjectContextHolder contextHolder;

    public static final String REPORT_RECIPE = "sbu30-report";

    private boolean isInitialReport = true;

    @GetMapping(path = "/spring-boot-upgrade", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String upgrade() {
        // Urgh... that's nasty
        if(!isInitialReport) {
            applyCommand.execute(contextHolder.getProjectContext(), REPORT_RECIPE);
        }
        isInitialReport = false;
        return reportHolder.getReport();
    }

    @PostMapping(path = "/spring-boot-upgrade", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String applyRecipes(@RequestParam("recipeNames[]") String[] recipeNames) {
        ProjectContext context = contextHolder.getProjectContext();
        List.of(recipeNames).forEach(recipeName -> applyCommand.execute(context, recipeName));
        applyCommand.execute(context, REPORT_RECIPE);
        return reportHolder.getReport();
    }

    @PostMapping(path = "/spring-boot-upgrade")
    @ResponseBody
    public void applyRecipes2(@RequestBody Recipe recipeNames) {
        ProjectContext context = contextHolder.getProjectContext();
        recipeNames.getRecipes().forEach(
                recipeName -> applyCommand.execute(context, recipeName)
        );
        applyCommand.execute(context, REPORT_RECIPE);
    }

    @Getter
    @Setter
    static class Recipe {
        private List<String> recipes;
    }
}
