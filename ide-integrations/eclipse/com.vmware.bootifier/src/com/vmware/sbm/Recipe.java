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

import org.springsource.ide.eclipse.commons.livexp.ui.Ilabelable;

public class Recipe implements Ilabelable {
    
    private String name;
    private String description;
    private String details;
    
    public Recipe() {}
    
    public Recipe(String name, String description, String detail) {
        super();
        this.name = name;
        this.description = description;
        this.details = detail;
    }

    @Override
    public String getLabel() {
        return name + (description != null && description.length() > 0 ? " - " + description : "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String detail) {
        this.details = detail;
    }

}
