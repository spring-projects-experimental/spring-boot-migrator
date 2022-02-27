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
package org.springframework.sbm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.eclipse.core.resources.IProject;
import org.springsource.ide.eclipse.commons.livexp.core.LiveExpression;
import org.springsource.ide.eclipse.commons.livexp.core.LiveVariable;
import org.springsource.ide.eclipse.commons.livexp.core.ValidationResult;
import org.springsource.ide.eclipse.commons.livexp.core.Validator;
import org.springsource.ide.eclipse.commons.livexp.ui.OkButtonHandler;
import org.springsource.ide.eclipse.commons.livexp.util.ExceptionUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MigrationWizardModel implements OkButtonHandler {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    
    final IProject project;
    
    LiveVariable<Recipe> chosenRecipe = new LiveVariable<>();
    
    LiveExpression<String> details = chosenRecipe.apply(r -> r == null ? "" : r.getDetails());
    
    LiveVariable<Recipe[]> recipes = new LiveVariable<>();
    
    LiveVariable<ValidationResult> loading = new LiveVariable<>(ValidationResult.OK);

    LiveExpression<ValidationResult> chosenRecipeValidator = new Validator() {
        
        {
            dependsOn(recipes);
            dependsOn(chosenRecipe);
        }

        @Override
        protected ValidationResult compute() {
            Recipe[] r = recipes.getValue();
            if (r != null && r.length > 0) {
                if (chosenRecipe.getValue() == null) {
                    return ValidationResult.warning("No recipe selected"); 
                }
            }
            return ValidationResult.OK;
        }
        
    };
    
    LiveExpression<Boolean> applyEnabler = chosenRecipeValidator.apply(vr -> vr != null && vr.isOk()).dependsOn(loading);
    
    public MigrationWizardModel(IProject project) {
        this.project = project;
    }
    
    void loadRecipes() {
        loading.setValue(ValidationResult.info("Loading..."));
        chosenRecipe.setValue(null);
        recipes.setValue(new Recipe[0]);
        try {
            HttpClient client = HttpClient.newBuilder().build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/scan?projectPath=" + project.getLocation()))
                    .POST(BodyPublishers.noBody()).build();

            HttpResponse<String> res = client.send(request, BodyHandlers.ofString());
            
            if (res.statusCode() >= 200 && res.statusCode() < 300) {
                recipes.setValue(OBJECT_MAPPER.readValue(res.body(), Recipe[].class));
            } else {
                throw new Exception("Http Response status code is not OK (" + res.statusCode() + ")");
            }
            loading.setValue(ValidationResult.OK);
        } catch (Exception e) {
            loading.setValue(ValidationResult.warning(ExceptionUtil.getMessage(e)));
        }
    }
    
    void applyRecipe() {
        loading.setValue(ValidationResult.info("Applying Recipe..."));
        try {
            HttpClient client = HttpClient.newBuilder().build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/apply?projectPath=" + project.getLocation() + "&recipe=" + chosenRecipe.getValue().getName()))
                    .POST(BodyPublishers.noBody()).build();

            HttpResponse<Void> res = client.send(request, BodyHandlers.discarding());
            
            if (res.statusCode() >= 400) {
                throw new Exception("Http Response status code is not OK (" + res.statusCode() + ")");
            }
            loading.setValue(ValidationResult.OK);
        } catch (Exception e) {
            loading.setValue(ValidationResult.warning(ExceptionUtil.getMessage(e)));
        }
    }

    @Override
    public void performOk() throws Exception {
        // Nothing special to do
    }
    
}
