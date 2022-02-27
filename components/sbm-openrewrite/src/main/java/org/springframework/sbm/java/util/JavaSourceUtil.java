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
package org.springframework.sbm.java.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSourceUtil {

    public static final Pattern PACKAGE_PATTERN = Pattern.compile("package ([\\w\\d\\.]*);");
    public static final Pattern CLASS_PATTERN = Pattern.compile("(class|interface|enum)\\s([\\w]+).*");

    public static String retrieveFullyQualifiedClassFileName(String code) {
        Matcher classMatcher = CLASS_PATTERN.matcher(code);

        String packageName = retrievePackageName(code);

        if (!classMatcher.find()) throw new RuntimeException("Could not extract classname from code '" + code + "'.");
        String className = classMatcher.group(2);
        if (packageName.isEmpty()) {
            return className + ".java";
        } else {
            return packageName.replace(".", File.separator) + File.separator + className + ".java";
        }

    }

    public static String retrievePackageName(String js) {
        Matcher packageMatcher = PACKAGE_PATTERN.matcher(js);
        String packageName = "";
        if (packageMatcher.find()) {
            packageName = packageMatcher.group(1);
        }
        return packageName;
    }
}
