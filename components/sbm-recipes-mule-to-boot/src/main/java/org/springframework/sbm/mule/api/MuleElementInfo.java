package org.springframework.sbm.mule.api;

import lombok.AllArgsConstructor;

import javax.xml.namespace.QName;

@AllArgsConstructor
public class MuleElementInfo {
    private QName qname;

    public String getNamespace() {
        String[] namespaceSplit = qname.getNamespaceURI().split("/");

        String simpleNamespace = namespaceSplit[namespaceSplit.length - 1];
        return (simpleNamespace.equals("") || simpleNamespace.equals("core")) ? "" : simpleNamespace;
    }


    public String getQualifiedTagName() {
        String namespace = getNamespace();
        return namespace.isEmpty() ? ("<" + getTagName() + "/>") : ("<" + namespace + ":" + getTagName() + "/>");
    }

    public String getTagName() {
        return qname.getLocalPart();
    }
}
