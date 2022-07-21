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
package org.springframework.sbm.boot.upgrade.common.actions;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Tree;
import org.openrewrite.marker.Markers;
import org.openrewrite.text.PlainText;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.StringProjectResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.function.Predicate.not;

public class CreateAutoconfigurationAction extends AbstractAction {

    private static final String SPRING_FACTORIES_PATH = "src/main/resources/META-INF/spring.factories";
    private static final String AUTO_CONFIGURATION_IMPORTS = "src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    @Override
    public void apply(ProjectContext context) {
        StringProjectResource springAutoconfigurationFile = new StringProjectResource(context.getProjectRootDirectory(), context.getProjectRootDirectory().resolve(AUTO_CONFIGURATION_IMPORTS), "");
        context.getProjectResources().add(springAutoconfigurationFile);
    }

}
