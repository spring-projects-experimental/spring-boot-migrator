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
package org.springframework.sbm.testhelper.common.utils;

import org.assertj.core.util.diff.DiffUtils;
import org.assertj.core.util.diff.Patch;

import java.util.List;
import java.util.stream.Collectors;

public class TestDiff {

    public static String of(String actual, String expected) {
        List<String> originalLines = actual.lines().collect(Collectors.toList());
        Patch<String> diff = DiffUtils.diff(originalLines, expected.lines().collect(Collectors.toList()));
        List<String> strings = DiffUtils.generateUnifiedDiff(actual, expected, originalLines, diff, 1000);
        String theDiff = strings.stream().collect(Collectors.joining(System.lineSeparator()));
        String headline = "\n\nHere's the diff between 1. (---) actual and 2. (+++) expected:\n\n";
        return headline + theDiff;
    }
}
