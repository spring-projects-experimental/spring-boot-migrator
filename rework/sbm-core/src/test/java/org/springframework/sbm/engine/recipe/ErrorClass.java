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
package org.springframework.sbm.engine.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;

import java.util.List;

public class ErrorClass extends org.openrewrite.Recipe {

    @Override
    public String getDisplayName() {
        return "NAME";
    }

    // FIXME: [Rewrite8 migration] This recipe uses the visit multiple sources method `visit(List<SourceFile> before, P p)`, needs to be migrated to use new introduced scanning recipe, please follow the migration guide here: https://docs.openrewrite.org/changelog/8-1-2-release
    @Override
    protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {
        ctx.getOnError().accept(new RuntimeException("A problem happened whilst visiting"));
        return super.visit(before, ctx);
    }
}
