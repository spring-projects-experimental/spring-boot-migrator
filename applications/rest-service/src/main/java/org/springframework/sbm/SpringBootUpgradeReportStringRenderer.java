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
import org.springframework.context.annotation.Primary;
import org.springframework.sbm.boot.upgrade.common.UpgradeReportUtil;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportRenderer;
import org.springframework.stereotype.Component;

/**
 * @author Fabian Krüger
 */
@Component
@Primary
public class SpringBootUpgradeReportStringRenderer implements SpringBootUpgradeReportRenderer {
    @Autowired
    private ReportHolder reportHolder;
    @Override
    public void processReport(String renderedReport) {
        String htmlReport = UpgradeReportUtil.renderHtml(renderedReport);
        String closingHeadTag = "</head>";

        String additionalHeader =
                "<link rel=\"stylesheet\" href=\"css/button.css\">\n" +
                "<script src=\"js/setup2.js\"></script>\n" +
                "<script src=\"js/site2.js\"></script>\n" +
                "<script src=\"js/java.min.js\"></script>\n";
        htmlReport = htmlReport.replace(closingHeadTag, additionalHeader + closingHeadTag);

        htmlReport = htmlReport.replace("</body>", """
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
                <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
                <script src="/js/recipe.js"></script>
                </body>
                """);
        reportHolder.setReport(htmlReport);
    }
}
