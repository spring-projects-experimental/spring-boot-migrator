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
package org.springframework.sbm.parsers;

import lombok.Getter;
import lombok.Setter;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;

import java.nio.charset.Charset;
import java.util.Collection;

public class JavaParserBuilder extends JavaParser.Builder {

	@Getter
	@Setter
	private JavaParser.Builder delegate = JavaParser.fromJavaVersion();

	@Override
	public JavaParser build() {
		return delegate.build();
	}

	@Override
	public JavaParser.Builder charset(Charset charset) {
		return delegate.charset(charset);
	}

	@Override
	public JavaParser.Builder classpath(Collection classpath) {
		return delegate.classpath(classpath);
	}

	@Override
	public JavaParser.Builder classpath(String... classpath) {
		return delegate.classpath(classpath);
	}

	@Override
	public JavaParser.Builder classpathFromResources(ExecutionContext ctx, String... classpath) {
		return delegate.classpathFromResources(ctx, classpath);
	}

	@Override
	public JavaParser.Builder classpath(byte[]... classpath) {
		return delegate.classpath(classpath);
	}

	@Override
	public JavaParser.Builder logCompilationWarningsAndErrors(boolean logCompilationWarningsAndErrors) {
		return delegate.logCompilationWarningsAndErrors(logCompilationWarningsAndErrors);
	}

	@Override
	public JavaParser.Builder typeCache(JavaTypeCache javaTypeCache) {
		return delegate.typeCache(javaTypeCache);
	}

	@Override
	public JavaParser.Builder dependsOn(Collection collection) {
		return delegate.dependsOn(collection);
	}

	@Override
	public JavaParser.Builder dependsOn(String... inputsAsStrings) {
		return delegate.dependsOn(inputsAsStrings);
	}

	@Override
	public JavaParser.Builder styles(Iterable iterable) {
		return delegate.styles(iterable);
	}

	@Override
	public String getDslName() {
		return delegate.getDslName();
	}

	//
	// public Supplier<JavaParser.Builder> getSupplier() {
	// return () -> builder;
	// }
	//
	// @Override
	// public JavaParser build() {
	// if(builder == null) {
	// builder = JavaParser.fromJavaVersion();
	// }
	// return builder.build();
	// }
	//
	// @Override
	// public JavaParser.Builder classpath(Collection classpath) {
	// return delegate.classpath(classpath);
	// }

}
