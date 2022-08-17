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
package org.springframework.sbm.build.migration.actions;

import lombok.Setter;
import org.springframework.sbm.build.api.RepositoryDefinition;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

/**
 * Adds Repository to pom.xml
 */
@Setter
public abstract class AddGenericRepositoryAction extends AbstractAction {

    private String id;
    private String url;
    private String name;
    private String layout;
    private Boolean snapshotsEnabled;
    private String snapshotsChecksumPolicy;
    private String snapShotsUpdatePolicy;
    private Boolean releasesEnabled;
    private String releasesChecksumPolicy;
    private String releasesUpdatePolicy;

    protected abstract void addRepository(ProjectContext context, RepositoryDefinition repositoryDefinition);

    @Override
    public void apply(ProjectContext context) {
        RepositoryDefinition.RepositoryDefinitionBuilder builder = RepositoryDefinition.builder();

        if (url == null) {
            throw new IllegalArgumentException("url is a mandatory field");
        }

        if (id == null) {
            throw new IllegalArgumentException("id is a mandatory field");
        }

        builder.id(id)
                .name(name)
                .url(url);

        if (snapshotsEnabled != null) {
            builder.snapshotsEnabled(snapshotsEnabled);
        }
        if (snapShotsUpdatePolicy != null) {
            builder.snapShotsUpdatePolicy(snapShotsUpdatePolicy);
        }
        if (snapshotsChecksumPolicy != null) {
            builder.snapshotsChecksumPolicy(snapshotsChecksumPolicy);
        }

        if (releasesEnabled != null) {
            builder.releasesEnabled(releasesEnabled);
        }
        if (releasesUpdatePolicy != null) {
            builder.releasesUpdatePolicy(releasesUpdatePolicy);
        }
        if (releasesChecksumPolicy != null) {
            builder.releasesChecksumPolicy(releasesChecksumPolicy);
        }
        if (layout != null) {
            builder.layout(layout);
        }

        RepositoryDefinition repository = builder.build();
        addRepository(context, repository);
    }
}

