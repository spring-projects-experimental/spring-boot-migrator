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
package org.springframework.sbm.build.migration;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.maven.cache.RocksdbMavenPomCache;
import org.springframework.sbm.engine.annotations.StatefulComponent;

import jakarta.annotation.PostConstruct;
import java.nio.file.Path;

@StatefulComponent
public class MavenPomCacheProvider {

	private MavenPomCache pomCache;

	@PostConstruct
	void postConstruct() {
		pomCache = rocksdb();
	}

	public MavenPomCache getPomCache() {
		return pomCache == null ? inMemory() : pomCache;
	}

	private MavenPomCache inMemory() {
		return new InMemoryMavenPomCache();
	}

	@NotNull
	private RocksdbMavenPomCache rocksdb() {
		return new RocksdbMavenPomCache(Path.of(".").toAbsolutePath());
	}

}
