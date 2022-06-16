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
package org.springframework.sbm.openrewrite;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MavenRefactoringTestHelper {
    public static void verifyChange(String pomXml, String refactoredPomXml, MavenVisitor visitor) {
        List<Result> results = applyVisitor(pomXml, visitor);
        assertEquals(refactoredPomXml, results.iterator().next().getAfter().printAll());
    }

    public static void verifyChange(String pomXml, String refactoredPomXml, Recipe recipe) {
        List<Result> results = applyRecipe(pomXml, recipe);
        assertEquals(refactoredPomXml, results.iterator().next().getAfter().printAll());
    }

    public static void verifyNoChange(String pomXml, String refactoredPomXml, MavenVisitor visitor) {
        List<Result> results = applyVisitor(pomXml, visitor);
        assertThat(results).isEmpty();
    }

    private static List<Result> applyRecipe(String pomXml, Recipe recipe) {
        List<Xml.Document> mavenList = MavenParser.builder().build().parse(pomXml);
        return recipe.run(mavenList);
    }

    private static List<Result> applyVisitor(String pomXml, MavenVisitor<ExecutionContext> visitor) {
        GenericOpenRewriteRecipe<MavenVisitor<ExecutionContext>> recipe = new GenericOpenRewriteRecipe<>(() -> visitor);
        return applyRecipe(pomXml, recipe);
    }

    public static void verifyNoChange(String pomXml, Recipe recipe) {
        List<Result> results = applyRecipe(pomXml, recipe);
        assertThat(results).isEmpty();
    }
}
