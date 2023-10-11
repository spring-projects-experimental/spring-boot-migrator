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
package org.springframework.sbm.jee.jpa.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.boot.properties.actions.AddSpringBootApplicationPropertiesAction;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.jee.jpa.api.Persistence;
import org.springframework.sbm.jee.jpa.api.PersistenceXml;
import org.springframework.sbm.jee.jpa.filter.PersistenceXmlResourceFilter;

import java.util.List;

public class MigratePersistenceXmlToApplicationPropertiesAction extends AbstractAction {

    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    @Override
    public void apply(ProjectContext context) {
        Module module = context.getApplicationModules().stream()
                .filter(m -> m.search(new PersistenceXmlResourceFilter("**/src/main/resources/**")).isPresent())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No file 'META-INF/persistence.xml' could be found."));

        PersistenceXml persistenceXml = module.search(new PersistenceXmlResourceFilter("**/src/main/resources/**")).get();
        List<SpringBootApplicationProperties> applicationProperties = module.search(new SpringBootApplicationPropertiesResourceListFilter());
        if (applicationProperties.isEmpty()) {
            AddSpringBootApplicationPropertiesAction addSpringBootApplicationPropertiesAction = new AddSpringBootApplicationPropertiesAction(executionContext);
            addSpringBootApplicationPropertiesAction.apply(module);
            applicationProperties = context.search(new SpringBootApplicationPropertiesResourceListFilter());
        }
        mapPersistenceXmlToApplicationProperties(applicationProperties.get(0), persistenceXml);
        applicationProperties.get(0).markChanged();
    }

    void mapPersistenceXmlToApplicationProperties(SpringBootApplicationProperties applicationProperties, PersistenceXml persistenceXml) {
        List<Persistence.PersistenceUnit> persistenceUnits = persistenceXml.getPersistence().getPersistenceUnit();
        persistenceUnits.stream()
            .filter(this::isPropertiesPresent)
            .forEach(persistenceUnit -> persistenceUnit.getProperties().getProperty()
                .forEach(p -> mapJpaPropertyToProperties(p, applicationProperties)));
    }

    private boolean isPropertiesPresent(Persistence.PersistenceUnit persistenceUnit) {
        return persistenceUnit.getProperties() != null && persistenceUnit.getProperties().getProperty() != null;
    }

    void mapJpaPropertyToProperties(Persistence.PersistenceUnit.Properties.Property property,
                                    SpringBootApplicationProperties applicationProperties) {
        new JpaHibernatePropertiesToSpringBootPropertiesMapper()
            .map(property)
            .ifPresent(kv-> applicationProperties.setProperty(kv.getComment(), kv.getPropertyName(), kv.getPropertyValue()));
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return context.search(new PersistenceXmlResourceFilter("**/src/main/resources/**")).isPresent();
    }
}
