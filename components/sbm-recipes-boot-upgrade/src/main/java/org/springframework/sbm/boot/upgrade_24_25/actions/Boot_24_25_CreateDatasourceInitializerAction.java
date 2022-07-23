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
package org.springframework.sbm.boot.upgrade_24_25.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.boot.upgrade_24_25.filter.CreateDatasourceInitializerAnalyzer;
import org.springframework.sbm.build.MultiModuleApplicationNotSupportedException;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.ProjectResource;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Boot_24_25_CreateDatasourceInitializerAction extends AbstractAction {

    @Autowired
    @JsonIgnore
    private Configuration configuration;

    @Override
    public void apply(ProjectContext context) {
        if (context.getApplicationModules().isSingleModuleApplication()) {
            ApplicationModule module = context.getApplicationModules().getRootModule();
            List<SpringBootApplicationProperties> applicationProperties = context.search(new SpringBootApplicationPropertiesResourceListFilter());
            applyToModule(module, applicationProperties);
        } else {
            throw new MultiModuleApplicationNotSupportedException("Action can only be applied to applications with single module.");
        }
    }

    private void applyToModule(ApplicationModule module, List<SpringBootApplicationProperties> applicationProperties) {
        CreateDatasourceInitializerAnalyzer analyzer = new CreateDatasourceInitializerAnalyzer();
        List<SpringBootApplicationProperties> sqlDataFileProperty = analyzer.findPropertyFilesContainingDataProperty(module, applicationProperties);

        List<SpringBootApplicationProperties> sqlSchemaFileProperty = analyzer.findPropertyFilesContainingSchemaProperty(module, applicationProperties);

        List<ProjectResource> dataAndSchemaFiles = analyzer.findSchemaAndDataFiles(module);

        Optional<ProjectResource> schemaFile = dataAndSchemaFiles.stream().filter(f -> f.getAbsolutePath().toString().endsWith("/schema.sql")).findFirst();
        Optional<ProjectResource> dataFile = dataAndSchemaFiles.stream().filter(f -> f.getAbsolutePath().toString().endsWith("/data.sql")).findFirst();

        List<SpringBootApplicationProperties> propertyFilesContainingDataUsername = analyzer.findPropertyFilesContainingDataUsernameProperty(module);
        List<SpringBootApplicationProperties> propertyFilesContainingDataPassword = analyzer.findPropertyFilesContainingDataPasswordProperty(module);

        if(sqlDataFileProperty.isEmpty()) {
            if(dataFile.isPresent()){
                // FIXME: handle multiple data and schema files
                String propertyName = "spring.sql.init.data-locations";
                // FIXME: handle properties in test resources
                String propertyValue = module.getMainJavaSourceSet().getBaseResourcesLocation(module.getProjectRootDirectory()).get().relativize(dataFile.get().getAbsolutePath()).normalize().toString();
                addProperty(applicationProperties, propertyName, propertyValue);
            }
        } else {
            sqlDataFileProperty.forEach(p -> {
                // rename spring.datasource.data to spring.sql.init.data-locations
                renameProperty(p, "spring.datasource.data", "spring.sql.init.data-locations");
            });
        }

        if( ! propertyFilesContainingDataUsername.isEmpty()) {
            renameProperty(propertyFilesContainingDataUsername, "spring.datasource.data-username","spring.sql.init.username");
        }

        if( ! propertyFilesContainingDataPassword.isEmpty()) {
            renameProperty(propertyFilesContainingDataUsername, "spring.datasource.data-password","spring.sql.init.password");
        }

        if(sqlSchemaFileProperty.isEmpty()) {
            if(schemaFile.isPresent()) {
                // add spring.datasource.schema=...
                // FIXME: handle properties in test resources
                String propertyName = "spring.datasource.schema";
                String propertyValue = module.getMainJavaSourceSet().getBaseResourcesLocation(module.getProjectRootDirectory()).get().relativize(schemaFile.get().getAbsolutePath()).normalize().toString();
                addProperty(applicationProperties, propertyName, propertyValue);
            } else {
                // should not happen as isApplicable() should return false
            }
        } else {
            // keep spring.datasource.schema
        }



        // add spring.datasource.data=data.sql if doesnt'exist
        String classPackage = calculatePackage(module);
        String dataSourceInitializerConfiguration = createDataSourceInitializerConfiguration(classPackage);
        module.getMainJavaSourceSet().addJavaSource(module.getProjectRootDirectory(), module.getMainJavaSourceSet().getJavaSourceLocation().getSourceFolder(), dataSourceInitializerConfiguration, classPackage);
        System.out.println(dataSourceInitializerConfiguration);
    }

    private void renameProperty(SpringBootApplicationProperties applicationProperties, String oldPropertyName, String newPropertyName) {
        applicationProperties.renameProperty(oldPropertyName, newPropertyName);
    }

    private void renameProperty(List<SpringBootApplicationProperties> applicationPropertiesList, String oldPropertyName, String newPropertyName) {
        applicationPropertiesList.forEach(p -> renameProperty(p, oldPropertyName, newPropertyName));
    }

    private void addProperty(List<SpringBootApplicationProperties> applicationPropertiesList, String propertyName, String propertyValue) {
        applicationPropertiesList.forEach(p -> p.setProperty(propertyName, propertyValue));
    }

    private String calculatePackage(ApplicationModule module) {
        return module.getMainJavaSourceSet().getJavaSourceLocation().getPackageName();
    }

    private String createDataSourceInitializerConfiguration(String classPakage) {
        try(StringWriter writer = new StringWriter()) {
            Map<String, Object> params = new HashMap<>();
            params.put("package", classPakage);
            params.put("springDatasourceSchemaProperty", "${spring.datasource.schema}");
            params.put("springDatasourceSchemaUsernameProperty","${spring.datasource.schema-username}");
            params.put("springDatasourceSchemaPasswordProperty", "${spring.datasource.schema-password}");

            Template template = configuration.getTemplate("DataSourceInitializerConfiguration.ftl");
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
