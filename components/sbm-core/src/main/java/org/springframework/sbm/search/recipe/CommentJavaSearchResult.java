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
package org.springframework.sbm.search.recipe;

import org.openrewrite.Tree;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.SearchResult;

import java.util.UUID;

public class CommentJavaSearchResult implements Marker {

    private SearchResult searchResult;
    private UUID id;

    public CommentJavaSearchResult(UUID id, @Nullable String description) {
        this.searchResult = new SearchResult(id, description);
//		super(id, recipe, description);
    }

    //	@Override
    public <P> String print(/*TreePrinter<P> printer, P p*/) {
        if (searchResult.getDescription() == null) {
            return "/* TODO: Comment should have been there */";
        } else {
            return String.format("/* %s */", searchResult.getDescription());
        }
    }


    @Override
    public UUID getId() {
        return searchResult.getId();
    }

    @Override
    public <T extends Tree> T withId(final UUID id) {
        CommentJavaSearchResult commentJavaSearchResult = this.id == id ? this : new CommentJavaSearchResult(id, searchResult.getDescription());
        return (T) commentJavaSearchResult;
    }

    public String getComment() {
        return searchResult.getDescription();
    }
}
