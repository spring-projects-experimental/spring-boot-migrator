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
package org.springframework.sbm.jee.wls;

import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.XmlVisitor;
import org.openrewrite.xml.tree.Xml;

class WlsEjbDdEnterpriseBeanVisitor extends XmlVisitor<EjbDeploymentDescriptor.EnterpriseBean> {

    private EjbDeploymentDescriptor.EnterpriseBean.TransactionDescriptor transactionDescriptor = null;

    public Xml visitTag(Xml.Tag tag, EjbDeploymentDescriptor.EnterpriseBean enterpriseBean) {
        XPathMatcher matcher = new XPathMatcher("/ejb-name");
        if (matcher.matches(getCursor())) {
            String ejbName = tag.getValue().get();
            enterpriseBean.setEjbName(ejbName);
        }

        XPathMatcher matcher2 = new XPathMatcher("/transaction-descriptor");

        if (matcher2.matches(getCursor())) {
            transactionDescriptor = enterpriseBean.addTransactionDescriptor();
        }

        XPathMatcher matcher3 = new XPathMatcher("/transaction-descriptor/trans-timeout-seconds");
        if (transactionDescriptor != null && matcher3.matches(getCursor())) {
            transactionDescriptor.setTransactionTimeoutSeconds(Integer.parseInt(tag.getValue().get()));
        }

        return super.visitTag(tag, enterpriseBean);
    }
}