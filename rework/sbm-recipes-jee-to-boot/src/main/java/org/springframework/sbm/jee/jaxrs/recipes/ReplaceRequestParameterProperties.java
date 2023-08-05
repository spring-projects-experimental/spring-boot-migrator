/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Recipe;

/**
 * @author Vincent Botteman
 */
public class ReplaceRequestParameterProperties extends Recipe {
    public ReplaceRequestParameterProperties() {
        doNext(new CopyAnnotationAttribute(
                "javax.ws.rs.DefaultValue", "value", "org.springframework.web.bind.annotation.RequestParam", "defaultValue")
        );
        doNext(new RemoveAnnotationIfAccompanied(
                "javax.ws.rs.DefaultValue", "org.springframework.web.bind.annotation.RequestParam"
        ));
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Migrate the properties of a request parameter: default value, ...";
    }
}