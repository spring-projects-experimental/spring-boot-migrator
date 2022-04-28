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
package org.springframework.sbm.boot.properties.api;

import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.properties.api.PropertiesSource;
import lombok.Getter;
import lombok.Setter;
import org.openrewrite.Tree;
import org.openrewrite.marker.Markers;
import org.openrewrite.properties.tree.Properties.File;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
public class SpringBootApplicationProperties extends PropertiesSource {

    private SpringProfile springProfile;

    public SpringBootApplicationProperties(Path absoluteProjectDir, File sourceFile, RewriteExecutionContext executionContext) {
        super(absoluteProjectDir, executionContext, sourceFile);
    }


    public SpringBootApplicationProperties(Path absoluteProjectDir, File sourceFile) {
        // FIXME: why is context required here and how should it be retrieved ?
        this(absoluteProjectDir, sourceFile, new RewriteExecutionContext());
    }

    public static SpringBootApplicationProperties newApplicationProperties(Path absoluteProjectDir, Path path) {
        File file = new File(Tree.randomId(), "", Markers.EMPTY, path, List.of(), "", null, false);
        SpringBootApplicationProperties springBootApplicationProperties = new SpringBootApplicationProperties(absoluteProjectDir, file);
        springBootApplicationProperties.markChanged();
        return springBootApplicationProperties;
    }

    public boolean isDefaultProperties() {
        return getAbsolutePath().getFileName().toString().equals("application.properties");
    }

}
