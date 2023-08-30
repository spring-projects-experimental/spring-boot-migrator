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
package org.springframework.sbm.build.migration.conditions;

class NoDependencyExistMatchingRegexTest {
//
//    private final NoDependencyExistMatchingRegex sut = new NoDependencyExistMatchingRegex(new ArrayList<>());
//
//    @Test
//    void shouldReturnTrueIfNoPatternMatches() {
//
//        final String d1 = "org.*";
//        final String d2 = "com.*";
//        sut.setDependencies(List.of(d1, d2));
//
//        OpenRewriteMavenBuildFile buildFile = mock(OpenRewriteMavenBuildFile.class);
//        when(buildFile.isRootBuildFile()).thenReturn(true);
//
//        ProjectContext projectContext = TestProjectContext.buildProjectContext()
//                .withProjectRoot(Path.of("."))
//                .withMockedBuildFile(buildFile)
//                .build();
//
//        when(buildFile.hasDeclaredDependencyMatchingRegex(d1)).thenReturn(false);
//        when(buildFile.hasDeclaredDependencyMatchingRegex(d2)).thenReturn(false);
//
//        final boolean hasDependency = sut.evaluate(projectContext);
//
//        assertThat(hasDependency).isTrue();
//    }
//
//    @Test
//    void shouldReturnFalseIfNoPatternMatches() {
//
//        final String d1 = "org.*";
//        final String d2 = "com";
//        sut.setDependencies(List.of(d1, d2));
//
//        OpenRewriteMavenBuildFile buildFile = mock(OpenRewriteMavenBuildFile.class);
//        when(buildFile.isRootBuildFile()).thenReturn(true);
//
//        ProjectContext projectContext = TestProjectContext.buildProjectContext()
//                .withProjectRoot(Path.of("."))
//                .withMockedBuildFile(buildFile)
//                .build();
//
//        when(buildFile.hasDeclaredDependencyMatchingRegex(d1)).thenReturn(false);
//        when(buildFile.hasDeclaredDependencyMatchingRegex(d2)).thenReturn(true);
//
//        final boolean hasDependency = sut.evaluate(projectContext);
//
//        assertThat(hasDependency).isFalse();
//    }
//
//    @Test
//    void shouldProvideDescription() {
//
//        final String d1 = "org.foo.bar";
//        final String d2 = "com.*";
//        sut.setDependencies(List.of(d1, d2));
//
//        final String description = sut.getDescription();
//
//        assertThat(description).contains("org.foo.bar", "com.*");
//    }
}