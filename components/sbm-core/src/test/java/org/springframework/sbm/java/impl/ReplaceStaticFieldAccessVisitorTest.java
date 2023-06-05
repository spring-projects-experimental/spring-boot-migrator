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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ReplaceStaticFieldAccessVisitorTest {

    @Test
    void replaceMediaTypeConstant() {

        String given = """
                import javax.ws.rs.core.MediaType;

                public class ControllerClass {
                    public String getHelloWorldJSON(String name) {
                        return MediaType.APPLICATION_XML;
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                public class ControllerClass {
                    public String getHelloWorldJSON(String name) {
                        return MediaType.APPLICATION_XML_VALUE;
                    }
                }
                """;

        StaticFieldAccessTransformer transform = foundConstant -> Optional.of(new StaticFieldAccessTransformer.StaticFieldRef("org.springframework.http.MediaType", "APPLICATION_XML_VALUE"));
        ReplaceStaticFieldAccessVisitor sut = new ReplaceStaticFieldAccessVisitor(transform);
        OpenRewriteTestSupport.verifyChange(sut, given, expected,
                "javax.ws.rs:javax.ws.rs-api:2.1.1",
                "org.springframework.boot:spring-boot-starter-web:2.4.2"
        );

        OpenRewriteTestSupport.verifyChangeIgnoringGiven(sut, given, expected,
                "javax.ws.rs:javax.ws.rs-api:2.1.1",
                "org.springframework.boot:spring-boot-starter-web:2.4.2"
        );
    }

    @Test
    void replaceMediaTypeConstant_noChange() {

        String given =
                "\nimport org.springframework.http.MediaType;\n" +
                        "\n" +
                        "public class ControllerClass {\n" +
                        "    public String getHelloWorldJSON(String name) {\n" +
                        "        return MediaType.APPLICATION_XML_VALUE;\n" +
                        "    }\n" +
                        "}\n";

        StaticFieldAccessTransformer transform = foundConstant -> Optional.of(new StaticFieldAccessTransformer.StaticFieldRef("org.springframework.http.MediaType", "APPLICATION_XML_VALUE"));
        ReplaceStaticFieldAccessVisitor sut = new ReplaceStaticFieldAccessVisitor(transform);

        OpenRewriteTestSupport.verifyNoChange(sut, given,
                "org.springframework.boot:spring-boot-starter-web:2.4.2"
        );
    }


}