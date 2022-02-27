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
package org.springframework.sbm.boot.asciidoctor;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Table {
    private List<String> headerCols = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Table table;

        public Builder headerCols(String... headerCols) {
            this.table = new Table();
            table.setHeaderCols(headerCols);
            return this;
        }

        public Builder row(String... cols) {
            table.addRow(Arrays.asList(cols));
            return this;
        }

        public Table build() {
            return table;
        }
    }

    private void addRow(List<String> cols) {
        this.rows.add(cols);
    }

    private void setHeaderCols(String... headerCols) {
        this.headerCols = Arrays.asList(headerCols);
    }
}
