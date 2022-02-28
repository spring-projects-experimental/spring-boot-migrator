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
package org.springframework.sbm.mule.api.toplevel;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.java.util.Helper;

import javax.xml.bind.JAXBElement;
import java.util.Set;

import static java.util.Collections.emptySet;

@AllArgsConstructor
public class UnknownTopLevelElement implements TopLevelElement {

    private JAXBElement element;

    @Override
    public Set<String> getRequiredImports() {
        return emptySet();
    }

    @Override
    public Set<String> getRequiredDependencies() {
        return emptySet();
    }

    @Override
    public String renderDslSnippet() {
        return "void "+ Helper.sanitizeForBeanMethodName(formMethodName())+"() {\n" +
                "//FIXME: element is not supported for conversion: " + getQualifiedTagName() + "\n }";
    }

    @NotNull
    private String formMethodName() {
        String namespace = getNamespace();
        String tagName = getTagName();

        if (namespace.equals("")) {

            return tagName;
        }
        else {
            return namespace + tagName.substring(0, 1).toUpperCase() + tagName.substring(1);
        }
    }

    private String getQualifiedTagName() {
        String namespace = getNamespace();
        return namespace.isEmpty() ? ("<" + getTagName() + "/>") : ("<" + namespace + ":" + getTagName() + "/>");
    }

    private String getTagName() {
        return element.getName().getLocalPart();
    }

    @NotNull
    private String getNamespace() {
        String[] namespaceSplit = element.getName().getNamespaceURI().split("/");

        String simpleNamespace = namespaceSplit[namespaceSplit.length - 1];
        return (simpleNamespace.equals("") || simpleNamespace.equals("core")) ? "" : simpleNamespace;

    }
}
