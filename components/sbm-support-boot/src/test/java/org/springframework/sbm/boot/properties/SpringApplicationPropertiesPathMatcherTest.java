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
package org.springframework.sbm.boot.properties;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

class SpringApplicationPropertiesPathMatcherTest {

    @Test
    void match1() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/main/resources/application.properties");
        assertThat(match.find()).isTrue();
    }

    @Test
    void match2() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/main/resources/application-some.properties");
        assertThat(match.find()).isTrue();
        assertThat(match.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX)).isEqualTo("some");
    }

    @Test
    void match_withResourceInSubDir() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/main/resources/META-INF/spring/application-sub-dir.properties");
        assertThat(match.find()).isTrue();
        assertThat(match.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX)).isEqualTo("sub-dir");
    }


    @Test
    void match3() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/main/resources/application-profile-with-dash.properties");
        assertThat(match.find()).isTrue();
        assertThat(match.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX)).isEqualTo("profile-with-dash");
    }

    @Test
    void match4() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/test/resources/application-profile-with-dash.properties");
        assertThat(match.find()).isTrue();
        assertThat(match.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX)).isEqualTo("profile-with-dash");
    }

    @Test
    void match_noMatch() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/src/main/resources/application.txt");
        assertThat(match.find()).isFalse();
    }

    @Test
    void match_outsideSourceDir() {
        SpringApplicationPropertiesPathMatcher sut = new SpringApplicationPropertiesPathMatcher();
        Matcher match = sut.match("/foo/bar/some/main/resources/application.properties");
        assertThat(match.find()).isFalse();
    }
}