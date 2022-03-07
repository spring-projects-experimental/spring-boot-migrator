package org.springframework.sbm.mule.actions.javadsl.translators;

import lombok.AllArgsConstructor;
import org.springframework.sbm.mule.api.MuleElementInfo;

@AllArgsConstructor
public class UnknownStatementTranslatorTemplate {

    private final MuleElementInfo elementInfo;

    public String render() {

        return "//FIXME: element is not supported for conversion: " + elementInfo.getQualifiedTagName();
    }
}
