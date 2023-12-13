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
package org.springframework.rewrite.project.resource.finder;

import lombok.Getter;
import org.springframework.rewrite.project.resource.ProjectResourceSet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericTypeFinder<T> implements ProjectResourceFinder<Optional<T>> {

	@Getter
	private final Class<T> type;

	public GenericTypeFinder(Class<T> type) {
		this.type = type;
	}

	@Override
	public Optional<T> apply(ProjectResourceSet projectResourceSet) {
		List<T> collect = projectResourceSet.stream()
			.filter(pr -> type.isAssignableFrom(pr.getClass()))
			.map(type::cast)
			.collect(Collectors.toList());
		if (collect.size() > 1) {
			throw new ResourceFilterException(
					String.format("Found more than one resource of type '%s'. Use %s instead.", type.getClass(),
							GenericTypeListFinder.class));
		}
		return collect.isEmpty() ? Optional.empty() : Optional.of(collect.get(0));
	}

}
