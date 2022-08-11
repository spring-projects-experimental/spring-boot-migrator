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
import org.springframework.sbm.boot.common.finder.MatchingMethod;
import org.springframework.sbm.boot.common.finder.SpringBeanMethodDeclarationFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonsMultipartResolverBeanFinder implements Sbm30_Finder<List<MatchingMethod>> {
    private static final String COMMONS_MULTIPART_RESOLVER_CLASS = "org.springframework.web.multipart.commons.CommonsMultipartResolver";

    @Override
    public @NotNull List<MatchingMethod> findMatches(ProjectContext context) {
        return context.search(new SpringBeanMethodDeclarationFinder(COMMONS_MULTIPART_RESOLVER_CLASS));
    }
}
