package org.springframework.sbm.mule.api.toplevel;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.java.util.Helper;

import javax.xml.bind.JAXBElement;
import java.util.Set;

import static java.util.Collections.emptySet;

@AllArgsConstructor
public class UnknownDefinition implements TopLevelDefinition {

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
