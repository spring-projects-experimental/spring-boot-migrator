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
package org.springframework.sbm.actions.spring.xml.migration;

import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BeanMethodFactoryTest {

    @Mock
    Helper helper;

    @Mock
    ListBeanHandler listBeanHandler;

    @Mock
    GenericBeanHandler genericBeanHandler;

    @InjectMocks
    BeanMethodFactory sut;

    @Test
    void createMembersForProperties() {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("SomeClass");
        MigrationContext migrationContext = new MigrationContext(null, null);
        PropertiesFile propertiesFile = new PropertiesFile(Path.of("./some.properties"));

        Property integerProperty = new Property("integer", "1");
        integerProperty.setType(Integer.class);
        propertiesFile.addProperty(integerProperty);

        Property stringProperty = new Property("string", "some string");
        stringProperty.setType(String.class);
        propertiesFile.addProperty(stringProperty);

        migrationContext.addPropertiesFile(propertiesFile);
        sut.createMembersForProperties(typeSpec, migrationContext.getPropertyFiles());
        String expected =
                "class SomeClass {\n" +
                "  @org.springframework.beans.factory.annotation.Value(\"${string}\")\n" +
                "  private java.lang.String string;\n" +
                "\n" +
                "  @org.springframework.beans.factory.annotation.Value(\"${integer}\")\n" +
                "  private java.lang.Integer integer;\n" +
                "}\n";
        assertThat(typeSpec.build().toString()).isEqualTo(expected);
    }
}