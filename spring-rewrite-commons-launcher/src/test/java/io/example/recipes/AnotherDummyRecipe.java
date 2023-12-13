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
package io.example.recipes;

import org.openrewrite.Contributor;
import org.openrewrite.Maintainer;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.config.RecipeExample;

import java.util.List;
import java.util.Set;

/**
 * @author Fabian Krüger
 */
public class AnotherDummyRecipe extends Recipe {

	@Option(displayName = "Property key", description = "The property key to add.", example = "some.example")
	private final String property;

	@Option(displayName = "Property value", description = "The value of the new property key.")
	private final String value;

	public AnotherDummyRecipe(String property, String value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public String getDisplayName() {
		return "AnotherDummyRecipe";
	}

	@Override
	public String getDescription() {
		return "Description of AnotherDummyRecipe";
	}

	@Override
	public List<Contributor> getContributors() {
		return List.of(new Contributor("Fabian Krüger", "some@email.com", 1),
				new Contributor("Mike Wazowski", "mike@monsterag.com", 1000));
	}

	@Override
	public List<Maintainer> getMaintainers() {
		return List.of(new Maintainer("Spring", null), new Maintainer("SBM", null));
	}

	@Override
	public List<RecipeExample> getExamples() {
		RecipeExample example1 = new RecipeExample();
		example1.setDescription("The recipe example description");
		example1.setParameters(List.of("param1", "param2"));

		RecipeExample.Source source1 = new RecipeExample.Source("foo", "bar", "the/path", "java");
		RecipeExample.Source source2 = new RecipeExample.Source("bim", "bam", "another/path", "kotlin");
		example1.setSources(List.of(source1, source2));

		RecipeExample example2 = new RecipeExample();
		example2.setDescription("The recipe example description");
		example2.setParameters(List.of("param1", "param2"));

		RecipeExample.Source source3 = new RecipeExample.Source("a", "b", "the/path", "java");
		RecipeExample.Source source4 = new RecipeExample.Source("0", "1", "another/path", "kotlin");
		example1.setSources(List.of(source3, source4));

		return List.of(example1, example2);
	}

	@Override
	public Set<String> getTags() {
		return Set.of("Java", "Example");
	}

}
