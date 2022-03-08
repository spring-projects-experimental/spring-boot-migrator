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
package org.springframework.sbm.test;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.parser.ProjectContextInitializer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProjectContextFileSystemTestSupport {

    /**
     * Take the project under
     *
     * {@code ./testcode/<projectRootDir>/given}
     *
     * and copy everything to
     *
     * {@code target/test-projects/<projectRootDir>}
     *
     * Initialize and return {@code ProjectContext}
     *
     * @param projectRootDir the dir under {@code ./testcode} which contains the testproject under {@code given}
     * @param beans optional list of Spring Beans to be added to the application context
     */
    public static ProjectContext createProjectContextFromDir(String projectRootDir, Class... beans) throws IOException {
        Path from = Path.of("./testcode").resolve(projectRootDir).resolve("given");
        Path to = Path.of("./target/test-projects/").resolve(projectRootDir);
        FileUtils.deleteDirectory(to.toFile());
        FileUtils.forceMkdir(to.toFile());
        FileUtils.copyDirectory(from.toFile(), to.toFile());
        // copy from `test-projects/<projectRootDir>/given' to 'target/test-projects/<projectRootDir>/

        List<Class> beanClasses = new ArrayList<>();
        beanClasses.addAll(Arrays.asList(beans));
        beanClasses.add(SpringBeanProvider.ComponentScanConfiguration.class);

        final Path projectRoot = to;
        final ProjectContextHolder projectContextHolder = new ProjectContextHolder();
        SpringBeanProvider.run(ctx -> {
                    ProjectContextInitializer projectContextBuilder = ctx.getBean(ProjectContextInitializer.class);
                    ScanCommand scanCommand = ctx.getBean(ScanCommand.class);
                    List<Resource> resources = scanCommand.scanProjectRoot(to.toString());
                    ProjectContext projectContext = projectContextBuilder.initProjectContext(projectRoot, resources, new RewriteExecutionContext());
                    projectContextHolder.setContext(projectContext);
                },
                beanClasses.toArray(new Class[]{})
        );
        return projectContextHolder.getContext();
    }

    @Getter
    @Setter
    private static class ProjectContextHolder {
        private ProjectContext context;
    }

}
