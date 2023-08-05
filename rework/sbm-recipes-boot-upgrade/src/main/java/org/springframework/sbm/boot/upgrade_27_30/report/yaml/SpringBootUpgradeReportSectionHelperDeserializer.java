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
package org.springframework.sbm.boot.upgrade_27_30.report.yaml;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.boot.upgrade_27_30.report.helper.ConditionOnlyHelper;
import org.springframework.sbm.engine.recipe.Condition;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Custom Jackson deserializer to deserialize SpringBootUpgradeSectionHelper with and without declared conditon.
 *
 * <pre>
 *  helper: fully.qualified.path.TheHelper
 * </pre>
 *
 * and
 *
 * <pre>
 *  helper:
 *    type: org.springframework.sbm.boot.upgrade_27_30.report.helper.ConditionOnlyHelper
 *    conditon:
 *      type: fully.qualified.ConditionClassName
 *      optionalParam: some-param
 * </pre>
 * @author Fabian Krüger
 */
public class SpringBootUpgradeReportSectionHelperDeserializer extends StdDeserializer<SpringBootUpgradeReportSectionHelper> {

    public SpringBootUpgradeReportSectionHelperDeserializer() {
        super(SpringBootUpgradeReportSectionHelper.class);
    }

    @Override
    public SpringBootUpgradeReportSectionHelper deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        try {
            // helper in section, 'helper: some.helper.ClassName'
            if(treeNode.getClass().isAssignableFrom(TextNode.class)) {
                Class<?> helperClass = classFromTextNode((TextNode) treeNode);
                if( ! SpringBootUpgradeReportSectionHelper.class.isAssignableFrom(helperClass)) {
                    throw new IllegalArgumentException("Helper must be of type " + SpringBootUpgradeReportSectionHelper.class.getName());
                }
                return (SpringBootUpgradeReportSectionHelper) helperClass.getConstructor().newInstance();
            }
            // helper in section with condition
            else if(treeNode.path("type") != null && treeNode.path("type").path("condition") != null) {
                if (treeNode.path("type").getClass().isAssignableFrom(TextNode.class)) {
                    Class<?> helperClass = classFromTextNode((TextNode) treeNode.path("type"));
                    if (helperClass.isAssignableFrom(ConditionOnlyHelper.class)) {
                        ConditionOnlyHelper coh = (ConditionOnlyHelper) helperClass.getConstructor().newInstance();
                        Condition condition = jsonParser.getCodec().treeToValue(treeNode.path("condition"), Condition.class);
                        coh.setCondition(condition);
                        return coh;
                    } else {
                        throw new IllegalArgumentException(String.format("Helper with condition must extend %s", ConditionOnlyHelper.class.getSimpleName()));
                    }
                } else {
                    throw exceptionWithExample();
                }
            }
            throw exceptionWithExample();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Class<?> classFromTextNode(TextNode treeNode) throws ClassNotFoundException {
        String className = treeNode.asText();
        Class<?> aClass = Class.forName(className);
        return aClass;
    }

    private IllegalArgumentException exceptionWithExample() {
        return new IllegalArgumentException(
                String.format(
                    """
                    Helper must be given.
                      1. Without condition:
                        helper: <fully qualified class name>
                      2. With condition:
                        helper:
                          type: <fully qualified name of class extending %s>
                          conditon:
                            type: <fully qualified name of condition class>
                            optionalParam: <param>
                    """,
                    ConditionOnlyHelper.class.getSimpleName())
        );
    }

}
