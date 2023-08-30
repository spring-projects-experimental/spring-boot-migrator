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
package org.springframework.sbm.project.parser;

class ResourceMarkerTest {

    static String path;
//
//    @Test
//    public void testLivesInMavenModuleShouldThrowExceptionWhenProjectDirIsrelative(){
//        ResourceMarker sut = new ResourceMarker();
//        Path absoluteProjectDir = Path.of("project-dir");
//        Path resourcePath = Path.of("project-dir/src/main/resources/schema.sql").toAbsolutePath();
//        Path mavenSourcePath = Path.of("pom.xml");
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(
//                        () -> sut.livesInMavenModule(absoluteProjectDir, resourcePath, mavenSourcePath)
//                );
//    }
//
//    @Test
//    public void testLivesInMavenModuleShouldThrowExceptionWhenResourcePathIsRelative(){
//        ResourceMarker sut = new ResourceMarker();
//        Path absoluteProjectDir = Path.of("project-dir").toAbsolutePath();
//        Path resourcePath = Path.of("project-dir/src/main/resources/schema.sql");
//        Path mavenSourcePath = Path.of("pom.xml");
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(
//                        () -> sut.livesInMavenModule(absoluteProjectDir, resourcePath, mavenSourcePath)
//                );
//    }
//
//    @Test
//    public void testLivesInMavenModuleShouldThrowExceptionWhenMavenSourcePathIsAbsolute(){
//        ResourceMarker sut = new ResourceMarker();
//        Path absoluteProjectDir = Path.of("project-dir").toAbsolutePath();
//        Path resourcePath = Path.of("project-dir/src/main/resources/schema.sql").toAbsolutePath();
//        Path mavenSourcePath = Path.of("pom.xml").toAbsolutePath();
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(
//                        () -> sut.livesInMavenModule(absoluteProjectDir, resourcePath, mavenSourcePath)
//                );
//    }
//
//
//    @Test
//    void testLivesInMavenModule(){
//        ResourceMarker sut = new ResourceMarker();
//        Path absoluteProjectDir = Path.of("project-dir").toAbsolutePath();
//        Path resourcePath = Path.of("project-dir/src/main/resources/schema.sql").toAbsolutePath();
//        Path mavenSourcePath = Path.of("pom.xml");
//        boolean livesInMavenModule = sut.livesInMavenModule(absoluteProjectDir, resourcePath, mavenSourcePath);
//        assertThat(livesInMavenModule).isTrue();
//    }
//
//    @Test
//    void testLivesInMavenModule2(){
//        ResourceMarker sut = new ResourceMarker();
//        Path absoluteProjectDir = Path.of("project-dir").toAbsolutePath();
//        Path resourcePath = Path.of("project-dir/another-module/src/main/resources/schema.sql").toAbsolutePath();
//        Path mavenSourcePath = Path.of("pom.xml");
//        boolean livesInMavenModule = sut.livesInMavenModule(absoluteProjectDir, resourcePath, mavenSourcePath);
//        assertThat(livesInMavenModule).isFalse();
//    }


}