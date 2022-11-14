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