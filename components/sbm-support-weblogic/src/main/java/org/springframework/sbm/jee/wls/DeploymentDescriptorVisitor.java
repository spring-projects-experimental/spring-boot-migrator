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

class DeploymentDescriptorVisitor extends XmlVisitor<EjbDeploymentDescriptor> {

    @Override
    public Xml visitTag(Xml.Tag tag, EjbDeploymentDescriptor ejbDeploymentDescriptor) {
        XPathMatcher matcher = new XPathMatcher("/weblogic-ejb-jar/weblogic-enterprise-bean");
        if (matcher.matches(getCursor())) {
            EjbDeploymentDescriptor.EnterpriseBean enterpriseBean = new EjbDeploymentDescriptor.EnterpriseBean();
            ejbDeploymentDescriptor.addEnterpriseBean(enterpriseBean);
            new WlsEjbDdEnterpriseBeanVisitor().visitTag(tag, enterpriseBean);
        }

        return super.visitTag(tag, ejbDeploymentDescriptor);
    }
}