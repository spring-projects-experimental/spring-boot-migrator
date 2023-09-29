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
package org.springframework.sbm.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.sbm.utils.OsAgnosticPathMatcher;
import org.springframework.util.PathMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class OsAgnosticPathMatcherTest {

    private final PathMatcher sut = new OsAgnosticPathMatcher();
    private String originalOsName;

    @BeforeEach
    void beforeEach() {
        originalOsName = System.getProperty("os.name");
    }

    @AfterEach
    void afterEach() {
        originalOsName = System.setProperty("os.name", originalOsName);
    }

    @CsvSource({
        "Win, **/*Foo.txt, my\\win\\path\\SomeFoo.txt, true",
        "OSX, **/*Foo.txt, my/osx/path/SomeFoo.txt, true",

        "Win, /**/*Foo.txt, my\\win\\path\\SomeFoo.txt, false",
        "OSX, /**/*Foo.txt, my/osx/path/SomeFoo.txt, false",

        "Win, /**/*Foo.txt, C:\\my\\win\\path\\SomeFoo.txt, true",
        "OSX, /**/*Foo.txt, /my/linux/path/SomeFoo.txt, true",

        "Win, **/*Foo.txt, E:\\my\\win\\path\\SomeFoo.txt, false",
        "OSX, **/*Foo.txt, /my/linux/path/SomeFoo.txt, false",

        "Win, /**/*Foo.txt, SOMEDRIVE:\\my\\..\\path\\SomeFoo.txt, true",
        "OSX, /**/*Foo.txt, /my/../path/SomeFoo.txt, true",

        "Win, /path/*Foo.txt, SOMEDRIVE:\\my\\..\\path\\SomeFoo.txt, true",
        "OSX, /path/*Foo.txt, /my/../path/SomeFoo.txt, true",

        "Win, /some/dir/SomeFile.txt, XYZ:\\some\\dir\\SomeFile.txt, true",
        "OSX, /some/dir/SomeFile.txt, /some/dir/SomeFile.txt, true"
    })
    @ParameterizedTest
    void testWindowsPathWithUnixPattern(String os, String pattern, String path, boolean expectMatch) {
        OsAgnosticPathMatcher sut = new OsAgnosticPathMatcher();
        if("Win".equals(os)) {
            System.setProperty("os.name", "Windows");
        }
        boolean match = sut.match(pattern, path);
        assertThat(match).isSameAs(expectMatch);
    }

}
