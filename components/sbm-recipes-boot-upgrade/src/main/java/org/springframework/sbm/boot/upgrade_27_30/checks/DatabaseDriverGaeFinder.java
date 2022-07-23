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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.java.tree.JavaType;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DatabaseDriverGaeFinder {

    @NotNull
    public Set<ApplicationModule> findMatches(ProjectContext context) {
        return context.getApplicationModules()
                .stream()
                .filter(this::hasClassAppEngineDriverOnClasspath)
                .collect(Collectors.toSet());
    }

    private boolean hasClassAppEngineDriverOnClasspath(ApplicationModule m) {
        return Stream.concat(m.getTestJavaSourceSet().stream(), m.getMainJavaSourceSet().stream())
                .anyMatch(js -> {
                    if (dependsOn(js, "com.google.appengine.api.rdbms.AppEngineDriver")) {
                        return true;
                    } else {
                        return false;
                    }
                });
    }


    private boolean dependsOn(JavaSource js, String s) {
        if (OpenRewriteJavaSource.class.isInstance(js)) {
            OpenRewriteJavaSource javaSource = OpenRewriteJavaSource.class.cast(js);
            return javaSource.getSourceFile().getMarkers().findFirst(org.openrewrite.java.marker.JavaSourceSet.class).get()
                    .getClasspath()
                    .stream()
                    .map(JavaType.FullyQualified::getFullyQualifiedName)
                    .anyMatch(fq -> s.equals(fq));
        }
        return false;
    }
}
