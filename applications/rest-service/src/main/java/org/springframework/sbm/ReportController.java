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
package org.springframework.sbm;

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

    @GetMapping(path = "/spring-boot-upgrade", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String upgrade() {
        // applyCommand.execute(contextHolder.getProjectContext(), "boot-2.7-3.0-upgrade-report2");
        return reportHolder.getReport();
    }

    @PostMapping(path = "/spring-boot-upgrade", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String applyRecipes(@RequestParam("recipeNames[]") String[] recipeNames) {
        /*
        ProjectContext context = contextHolder.getProjectContext();
        List.of(recipeNames).forEach(recipeName -> applyCommand.execute(context, recipeName));
        applyCommand.execute(context, "boot-2.7-3.0-upgrade-report2");

         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return reportHolder.getReport();
    }

    @PostMapping(path = "/spring-boot-upgrade")
    @ResponseBody
    public void applyRecipes2(@RequestBody String recipeNames) {
        /*
        ProjectContext context = contextHolder.getProjectContext();
        List.of(recipeNames).forEach(recipeName -> applyCommand.execute(context, recipeName));
        applyCommand.execute(context, "boot-2.7-3.0-upgrade-report2");

         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}