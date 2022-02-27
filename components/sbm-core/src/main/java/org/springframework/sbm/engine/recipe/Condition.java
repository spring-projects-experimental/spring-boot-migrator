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
package org.springframework.sbm.engine.recipe;

import org.springframework.sbm.engine.context.ProjectContext;

public interface Condition {

    Condition FALSE = new Condition() {

        @Override
        public String getDescription() {
            return "FALSE";
        }

        @Override
        public boolean evaluate(ProjectContext context) {
            return false;
        }
    };
    Condition TRUE = new Condition() {

        @Override
        public String getDescription() {
            return "TRUE";
        }

        @Override
        public boolean evaluate(ProjectContext context) {
            return true;
        }
    };

    default Condition or(Condition other) {
        return new Condition() {

            @Override
            public String getDescription() {
                if (Condition.this.getDescription() == null) {
                    return other.getDescription();
                } else if (other.getDescription() == null) {
                    return Condition.this.getDescription();
                } else {
                    return Condition.this.getDescription() + " or " + other.getDescription();
                }
            }

            @Override
            public boolean evaluate(ProjectContext context) {
                return Condition.this.evaluate(context) || other.evaluate(context);
            }
        };
    }

    String getDescription();

    boolean evaluate(ProjectContext context);
}
