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

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;

@Configuration
@RequiredArgsConstructor
public class SpringBootMigratorRunner implements ApplicationRunner {

    private final ScanCommand scanCommand;
    private final ProjectContextHolder contextHolder;
    private final ApplyCommand applyCommand;

    @Override
    public void run(ApplicationArguments args) {
        if (args.getSourceArgs().length == 0) {
            System.err.println("PLease provide the path to the application as parameter.");
            return;
        }
        String applicationPath = args.getSourceArgs()[0];
        System.out.println("Scanning " + applicationPath);
        ProjectContext context = scanCommand.execute(applicationPath);
        contextHolder.setProjectContext(context);
        applyCommand.execute(contextHolder.getProjectContext(), "boot-2.7-3.0-upgrade-report2");
        System.out.println("finished scan. Please open: http://localhost:8080/spring-boot-upgrade");
    }


}
