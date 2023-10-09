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
package org.springframework.sbm.utils;

import org.springframework.util.StringUtils;

import java.nio.file.Path;

public class LinuxWindowsPathUnifier {

    public static String unifyPath(Path path) {
        return unifyPath(path.toString());
    }

    public static String unifyPath(String path) {
        path = StringUtils.cleanPath(path);
        if (isWindows()) {
            path = transformToLinuxPath(path);
        }
        return path;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    private static String transformToLinuxPath(String path) {
        return path.replaceAll("^[\\w]+:\\/?", "/");
    }
}
