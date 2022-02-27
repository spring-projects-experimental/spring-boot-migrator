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
package org.springframework.sbm.sccs;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class MigrateToSpringCloudConfigServer extends AbstractAction {

    @Autowired
    private MigrateToSpringCloudConfigServerHelper helper;

    @Override
    public void apply(ProjectContext context) {
        // find available spring profiles
        List<SpringBootApplicationProperties> bootApplicationProperties = helper.findAllSpringApplicationProperties(context);

        // collect defined properties for all profiles
//        List<SpringProfile> springProfiles = helper.findAllSpringProfiles(context);

        // create Git repo for sccs
        Path sccsProjectDir = helper.initializeSccsProjectDir(context.getProjectRootDirectory());

        // copy properties files to config project
        helper.copyFiles(bootApplicationProperties, sccsProjectDir);

        // commit properties to repo
        helper.commitProperties(sccsProjectDir, bootApplicationProperties);

        // configure connection to SCCS
        helper.configureSccsConnection(bootApplicationProperties);

        // delete all but default application.properties
        helper.deleteAllButDefaultProperties(bootApplicationProperties);
    }

}
