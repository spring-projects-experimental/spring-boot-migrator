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
package org.springframework.sbm.boot;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkdownTable {

    private static TableDefinition tableDefinition;

    public static ColumnDefinition withHeaderColumns() {
        tableDefinition = new TableDefinition();
        ColumnDefinition columnDefinition = new ColumnDefinition();
        tableDefinition.setColumnDefinition(columnDefinition);
        return columnDefinition;
    }

    public static class ColumnDefinition {
        private List<Column> columns = new ArrayList<>();

        public ColumnDefinition col(String text) {
            Column col = new Column(text);
            this.columns.add(col);
            return this;
        }

        public RowDefinition withRows() {
            RowDefinition rowDefinition = new RowDefinition();
            return rowDefinition;
        }
    }

    private static class Column {
        private final String text;

        public Column(String text) {
            this.text = text;
        }
    }

    @Getter
    public static class RowDefinition {

        private List<Column> colTexts = new ArrayList<>();

        public RowDefinition row(String... colText) {
            RowDefinition rowDefinition = new RowDefinition();
            Arrays.asList(colText).forEach(col -> {
                rowDefinition.colTexts.add(new Column(col));
            });

            tableDefinition.rowDefinitions.add(rowDefinition);
            return rowDefinition;
        }

        public String render() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            // start table
            stringBuilder.append("|===").append("\n");
            // render header row
            tableDefinition.columnDefinition.columns.forEach(columnHeader -> {
                stringBuilder.append("| ").append(columnHeader.text);
            });
            stringBuilder.append("\n\n");
            // render table formatting row
            // render table rows
            tableDefinition.rowDefinitions.stream().forEach(rowDef -> {
                rowDef.colTexts.forEach(col -> {
                    stringBuilder.append(" |").append(col.text);
                });
                stringBuilder.append("\n");
            });
            // end table
            stringBuilder.append("|===").append("\n");
            return stringBuilder.toString();
        }
    }

    public static class TableDefinition {
        private ColumnDefinition columnDefinition;
        private List<RowDefinition> rowDefinitions = new ArrayList<>();

        public void setColumnDefinition(ColumnDefinition columnDefinition) {
            this.columnDefinition = columnDefinition;
        }

        public void addRowDefinition(RowDefinition rowDefinition) {
            this.rowDefinitions.add(rowDefinition);
        }
    }
}
