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
package org.springframework.sbm.parsers;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
@Component
class MavenModelReader {
    public Model readModel(Resource mavenPomFile) {
        try {
            return new MavenXpp3Reader().read(ResourceUtil.getInputStream(mavenPomFile));
        } catch (IOException | XmlPullParserException e) {
            Path path = ResourceUtil.getPath(mavenPomFile);
            throw new RuntimeException("Could not read Maven model from resource '%s'".formatted(path.toString()), e);
        }
    }
}
