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

import org.springframework.sbm.utils.LinuxWindowsPathUnifier;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpringApplicationPropertiesPathMatcher {
    public static final int PROFILE_GROUP_INDEX = 2;
    private String regex = "src/(main|test)/resources/[/\\w-]*application[-]{0,1}([-\\w]*).properties$";
    private Pattern profilePattern = Pattern.compile(regex);

    public Matcher match(String path) {
        String unifiedPath = new LinuxWindowsPathUnifier().unifyPath(path);
        return profilePattern.matcher(unifiedPath);
    }
}
