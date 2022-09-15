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

package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.RecipeRun;
import org.openrewrite.Result;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangeTypeTest {

    @Test
    void changeTypeTest() {
        String javaSource = ""
                + "import java.util.Locale;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.language(\"ua\");\n"
                + "        b.language(Locale.ITALY);\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        List<Path> classpathFiles = OpenRewriteTestSupport.getClasspathFiles("javax:javaee-api:8.0", "org.springframework:spring-web:5.3.18");

        JavaParser javaParser = JavaParser.fromJavaVersion()
                .classpath(classpathFiles)
                .build();
        List<J.CompilationUnit> compilationUnits = javaParser.parse(javaSource);

        RecipeRun run = new ChangeType("javax.ws.rs.core.Response$ResponseBuilder",
                                       "org.springframework.http.ResponseEntity$BodyBuilder", false).run(compilationUnits);

        System.out.println(run.getResults().get(0).getAfter().printAll());
    }
}
