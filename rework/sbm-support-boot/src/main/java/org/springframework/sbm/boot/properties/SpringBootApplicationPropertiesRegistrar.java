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
package org.springframework.sbm.boot.properties;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.properties.tree.Properties;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.api.SpringProfile;
import org.springframework.sbm.common.util.OsAgnosticPathMatcher;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import java.nio.file.Path;
import java.util.regex.Matcher;

@Component
@RequiredArgsConstructor
public class SpringBootApplicationPropertiesRegistrar implements ProjectResourceWrapper<SpringBootApplicationProperties> {

    private static final String PATTERN = "/**/src/main/resources/application*.properties";
    public static final String PATTERN1 = "/**/src/main/resources/config/application*.properties";
    private PathMatcher pathMatcher = new OsAgnosticPathMatcher();
    private final SpringApplicationPropertiesPathMatcher springApplicationPropertiesPathMatcher;
    private final ExecutionContext executionContext;

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        boolean assignableFrom = Properties.File.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass());
        boolean match = pathMatcher.match(PATTERN, rewriteSourceFileHolder.getAbsolutePath().toString()) || pathMatcher.match(PATTERN1, rewriteSourceFileHolder.getAbsolutePath().toString());
        return match && assignableFrom;
    }

    @Override
    public SpringBootApplicationProperties wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        // TODO: How to pass current executionContext ?
        Properties.File properties = Properties.File.class.cast(rewriteSourceFileHolder.getSourceFile());
        SpringBootApplicationProperties springBootApplicationProperties = new SpringBootApplicationProperties(rewriteSourceFileHolder.getAbsoluteProjectDir(), properties, executionContext);
        SpringProfile springProfile = extractProfileFromFilename(springBootApplicationProperties.getAbsolutePath());
        springBootApplicationProperties.setSpringProfile(springProfile);
        return springBootApplicationProperties;
    }

    private SpringProfile extractProfileFromFilename(Path absolutePath) {
        Matcher matcher = springApplicationPropertiesPathMatcher.match(absolutePath.toString());
        String profile = "default";
        if (matcher.find()) {
            profile = matcher.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX);
            if(profile.isEmpty()) {
                profile = "default";
            }
        }
        return new SpringProfile(profile);
    }

//    private final RewriteExecutionContext executioncontext;

    public static final Path APPLICATION_PROPERTIES_PATH = Path.of("src/main/resources/application.properties");


/*
    public void createPropertiesProjections(ProjectResourceSet projectResources) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new ArrayList<>();
        PropertiesParser parser = new PropertiesParser();
        for (int i = 0; i < projectResources.size(); i++) {
            ModifiableProjectResource<?> projectResource = projectResources.get(i);
            Matcher matcher = springApplicationPropertiesPathMatcher.match(projectResource.getAbsolutePath().toString());
            if (matcher.find()) {
                Path sourceFile = projectResource.getAbsolutePath();
                String profile = matcher.group(SpringApplicationPropertiesPathMatcher.PROFILE_GROUP_INDEX);
                if(profile.isEmpty()) {
                    profile = "default";
                }
                List<Properties.File> parsedFiles = parser.parseInputs(List.of(new Input(sourceFile, () -> {
                    return new ByteArrayInputStream(projectResource.print().getBytes(StandardCharsets.UTF_8));
                })), null, executioncontext);
                if (!parsedFiles.isEmpty()) {
                    SpringBootApplicationProperties properties = new SpringBootApplicationProperties(parsedFiles.get(0), executioncontext);
                    properties.setSpringProfile(new SpringProfile(profile));
                    projectResources.replace(sourceFile.toAbsolutePath(), properties);
                } else {
                    throw new RuntimeException(String.format("Could not parse properties file '%s'", sourceFile.toAbsolutePath()));
                }
            }
        }
    }

 */
}
