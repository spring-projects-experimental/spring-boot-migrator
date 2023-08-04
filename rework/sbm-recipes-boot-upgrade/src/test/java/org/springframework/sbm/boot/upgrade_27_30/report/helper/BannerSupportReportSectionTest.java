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

package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;


public class BannerSupportReportSectionTest {

    @Test
    public void rendersBannerSupportInformation() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .withProjectResource("src/main/resources/banner.gif", "gif-banner")
                .withProjectResource("src/main/resources/banner.jpg", "jpg-banner")
                .withProjectResource("src/main/com/test/banner.java","class banner {}")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Banner support")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === Banner support
                                
                                ==== What Changed
                                Support for image-based application banners has been removed. banner.gif, banner.jpg, and banner.png
                                files are now ignored and should be replaced with a text-based banner.txt file.
                                                                
                                ==== Why is the application affected
                                The scan found banner image files here:
                                                                
                                * `<PATH>/src/main/resources/banner.gif`
                                * `<PATH>/src/main/resources/banner.jpg`
                                                        
                                ==== Remediation
                                 
                                                        
                                ===== Remove image banner
                                Remove these image banners\s
                                                        
                                * `<PATH>/src/main/resources/banner.gif`
                                * `<PATH>/src/main/resources/banner.jpg`
                                                        
                                                        
                                                        
                                ++++
                                <div class="run-a-recipe" recipe="sbu30-remove-image-banner">
                                </div>
                                ++++
                                ===== Replace image banner
                                Replace these banners\s
                                                        
                                * `<PATH>/src/main/resources/banner.gif`
                                * `<PATH>/src/main/resources/banner.jpg`
                                                        
                                with `banner.txt`
                                                
                                                  
                                """);
    }
}
