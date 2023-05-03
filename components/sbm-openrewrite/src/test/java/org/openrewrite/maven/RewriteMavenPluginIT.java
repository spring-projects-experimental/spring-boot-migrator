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
package org.openrewrite.maven;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing the OpenRewrite Maven Plugin to verify expectations about Markers.
 *
 * Using Maven Integration Testing Framework
 * https://khmarbaise.github.io/maven-it-extension/itf-documentation/usersguide/usersguide.html
 *
 * @author Fabian Krüger
 */
@MavenJupiterExtension
public class RewriteMavenPluginIT {
    @MavenTest
    @MavenGoal("rewrite:discover") //${project.groupId}:${project.artifactId}:${project.version}:compare-dependencies")
    void test_plugin(MavenExecutionResult result) {
        Assertions.assertThat(true).isFalse();
    }
}
