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
package org.springframework.sbm.mule.resources.filter;

import org.springframework.sbm.common.api.TextResource.TextSource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.openrewrite.text.PlainText;

import java.util.List;
import java.util.stream.Collectors;

public class RamlFileProjectResourceFilter implements ProjectResourceFinder<List<TextSource>> {
    @Override
    public List<TextSource> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet.stream()
                .filter(r -> r.getAbsolutePath().toString().endsWith(".raml"))
                .filter(r -> PlainText.class.isInstance(r.getSourceFile()))
                .map(r -> new TextSource(r.getAbsoluteProjectDir(), (PlainText)r.getSourceFile()))
                .collect(Collectors.toList());
    }
}
