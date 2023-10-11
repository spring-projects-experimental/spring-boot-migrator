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
package org.springframework.sbm.jee.jpa.filter;

import org.springframework.sbm.jee.jpa.api.PersistenceXml;
import org.springframework.sbm.project.resource.GenericTypeFilter;
import org.springframework.sbm.project.resource.filter.ResourceFilterException;

// FIXME: what if persistence.xml in src/test/resources also exists?!
public class PersistenceXmlResourceFilter extends GenericTypeFilter<PersistenceXml> {

    /**
     * Apply the filter to all resources.
     *
     * @see org.springframework.sbm.project.resource.filter.GenericTypeListFilter
     * @throws ResourceFilterException when more than one resource was found.
     */
    public PersistenceXmlResourceFilter() {
        super(PersistenceXml.class);
    }

    /**
     * Apply the filter only to resources matching the {@code directoryPattern}.
     * The {@code directoryPattern} is used as pattern for {@link java.nio.file.PathMatcher}.
     *
     * @see org.springframework.sbm.project.resource.filter.GenericTypeListFilter
     * @throws ResourceFilterException when more than one resource was found.
     */
    public PersistenceXmlResourceFilter(String directoryPattern) {
        super(PersistenceXml.class, directoryPattern);
    }

}
