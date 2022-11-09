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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportFileSystemRenderer;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportRenderer;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.events.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class SpringBootMigratorServiceApp  implements ApplicationRunner {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("PLease provide the path to the application as parameter.");
            return;
        }
        SpringApplication.run(SpringBootMigratorServiceApp.class, args);
    }

    @Autowired
    private ScanCommand scanCommand;
    @Autowired
    private ApplyCommand applyCommand;
    @Autowired
    private SpringBootUpgradeReportRenderer upgradeReportRenderer;
    @Autowired
    private ProjectContextHolder contextHolder;

    @Autowired
    private ReportHolder reportHolder;

    @Override
    public void run(ApplicationArguments args) {
        String applicationPath = args.getSourceArgs()[0];
        System.out.println("Scanning " + applicationPath);
        ProjectContext context = scanCommand.execute(applicationPath);
        contextHolder.setProjectContext(context);
        System.out.println("finished scan. Please open: http://localhost:8080/spring-boot-upgrade");
    }


}

@Controller
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
        applyCommand.execute(contextHolder.getProjectContext(), "boot-2.7-3.0-upgrade-report2");
        return reportHolder.getReport();
    }

    @PostMapping(path = "/spring-boot-upgrade", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String applyRecipe(@RequestParam  String recipeName) {
        ProjectContext context = contextHolder.getProjectContext();
        applyCommand.execute(context, recipeName);
        applyCommand.execute(context, "boot-2.7-3.0-upgrade-report2");
        return reportHolder.getReport();
    }
}
