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
package org.junit.rules;

/**
 * "Fake" class used as a replacement for Junit4-dependent classes.
 * See more at: <a href="https://github.com/testcontainers/testcontainers-java/issues/970">
 * GenericContainer run from Jupiter tests shouldn't require JUnit 4.x library on runtime classpath
 * </a>.
 */
@SuppressWarnings("unused")
public interface TestRule {
}