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
package org.springframework.sbm.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.Recipe;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
@Getter
public class ScanResult{
    private final List<Recipe> applicableRecipes;
    private final Path scannedDir;
    private final int timeElapsed;
    private final PreconditionVerificationResult result;

    public ScanResult(PreconditionVerificationResult result, Path scannedDir, int timeElapsed) {
        this.result = result;
        this.scannedDir = scannedDir;
        this.timeElapsed = timeElapsed;
        applicableRecipes = null;
    }

    public ScanResult(List<Recipe> recipes, Path scannedDir, int timeElapsed) {
        this.scannedDir = scannedDir;
        this.timeElapsed = timeElapsed;
        result = null;
        applicableRecipes = recipes;
    }
}
