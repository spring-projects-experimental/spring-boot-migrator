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
package org.springframework.sbm.boot.properties.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;

import java.nio.file.Path;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AddSpringBootApplicationPropertiesAction extends AbstractAction {

    public static final Path APPLICATION_PROPERTIES_PATH = Path.of("src/main/resources/application.properties");
    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    public AddSpringBootApplicationPropertiesAction(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public void apply(ProjectContext context) {
		if(context.getApplicationModules().isSingleModuleApplication()) {
			Module rootModule = context.getApplicationModules().getRootModule();
			this.apply(rootModule);
		} else {
			context.getApplicationModules()
					.getTopmostApplicationModules()
					.forEach(this::apply);
		}
    }

    public void apply(Module module) {
        SpringBootApplicationProperties springBootApplicationProperties = SpringBootApplicationProperties
				.newApplicationProperties(
						module.getProjectRootDirectory(),
						module.getModulePath().resolve(APPLICATION_PROPERTIES_PATH),
                        executionContext
				);
        module.getMainResourceSet().addResource(springBootApplicationProperties);
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return context.search(new SpringBootApplicationPropertiesResourceListFilter()).isEmpty();
    }
}
