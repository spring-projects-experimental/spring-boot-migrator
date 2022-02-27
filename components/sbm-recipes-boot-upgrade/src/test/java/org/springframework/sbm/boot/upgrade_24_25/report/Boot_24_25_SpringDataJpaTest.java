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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.junit.jupiter.api.Test;

class Boot_24_25_SpringDataJpaTest {

    @Test
    void isApplicable_withCallsToGetOne_shouldReturnTrue() {
        String model =
                "package com.example;\n" +
                "public class Tag {}";
        String repo =
                "package com.example;\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "\n" +
                "public interface TagRepository extends JpaRepository<Tag, Long> {\n" +
                "    public Tag getOne(Long id);\n" + // FIXME: hack: JpaRepository.getOne() should be found in latest Rewrite, see https://rewriteoss.slack.com/archives/G01J94KRH70/p1636732658014900
                "}";
        String caller =
                "package com.example;\n" +
                "public class Caller {\n" +
                "    TagRepository repo;" +
                "    public void call() {\n" +
                "        repo.getOne(1L);\n" +
                "    }\n" +
                "}";

        // TODO: add tests after merge with upgrade branch update-to-rewrite-7.14.0

    }

    @Test
    void build() {
    }
}