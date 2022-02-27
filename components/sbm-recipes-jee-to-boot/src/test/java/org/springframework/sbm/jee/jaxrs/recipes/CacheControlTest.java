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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheControlTest {

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    SwapCacheControl r = new SwapCacheControl();
                    context.getProjectJavaSources().apply(r);
                }
            };

    @Test
    void general() {

        String javaSource = ""
                + "import javax.ws.rs.core.CacheControl;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public CacheControl test() {\n"
                + "        CacheControl c = new CacheControl();\n"
                + "        c.setSMaxAge(3600);\n"
                + "        return c;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.CacheControl;\n"
                + "\n"
                + "import java.util.concurrent.TimeUnit;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public CacheControl test() {\n"
                + "        CacheControl c = CacheControl.empty();\n"
                + "        c.sMaxAge(3600, TimeUnit.SECONDS);\n"
                + "        return c;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:8.0")
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

}
