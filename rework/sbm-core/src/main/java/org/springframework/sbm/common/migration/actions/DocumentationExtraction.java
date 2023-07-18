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
package org.springframework.sbm.common.migration.actions;

import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.impl.OpenRewriteType;

import java.nio.file.Path;
import java.util.List;

public class DocumentationExtraction extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        Path rootDirectory = context.getProjectRootDirectory();

        context.getModules().forEach(module -> {
            Path sourcePath = module.getBaseJavaSourceLocation().getSourceFolder();
            Path sourceDir = rootDirectory.relativize(sourcePath);
            module.getMainJavaSources().stream()
                    .filter(js -> js.getResource().getAbsolutePath().endsWith(sourceDir.resolve(js.getPackageName().replace(".", "/") + "/" + js.getResource().getAbsolutePath().getFileName())))
                    .map(JavaSource::getTypes)
                    .flatMap(List::stream)
                    .filter(t -> OpenRewriteType.class.isAssignableFrom(t.getClass()))
                    .map(OpenRewriteType.class::cast)
                    .filter(this::filterActions)
                    .forEach(t -> {
                        Class<?> actionClass = getaClass(t);
                        List<? extends Member> members = t.getMembers();
                        String render = render(actionClass.getName(), members);
                        System.out.println(render);
                    });
        });

    }

    private boolean filterActions(OpenRewriteType javaSource) {
        Class<?> aClass = getaClass(javaSource);
        return Action.class.isAssignableFrom(aClass);
    }

    private String render(String actionClass, List<? extends Member> members) {
        StringBuilder sb = new StringBuilder();
        sb.append("| `").append(actionClass).append("`").append(System.lineSeparator());
        sb.append("| ");
        members.forEach(m ->
                sb.append("`").append(m.getName()).append("`")
                        .append(" (").append("`").append(m.getTypeFqName()).append("`)")
                        .append(System.lineSeparator())
        );
        sb.append("|").append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return context.getProjectJavaSources().asStream()
                .flatMap(js -> js.getTypes().stream())
//                .flatMap(t -> t.getImplements().stream())
                .filter(t -> OpenRewriteType.class.isAssignableFrom(t.getClass()))
                .map(OpenRewriteType.class::cast)
                .map(this::getaClass)
                .anyMatch(Action.class::isAssignableFrom);
    }

    @NotNull
    private Class<?> getaClass(OpenRewriteType t) {
        try {
            return Class.forName(t.getClassDeclaration().getType().getFullyQualifiedName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
