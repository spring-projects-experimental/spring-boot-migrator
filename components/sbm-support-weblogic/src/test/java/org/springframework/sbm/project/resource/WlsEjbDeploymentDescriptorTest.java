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
package org.springframework.sbm.project.resource;

import org.springframework.sbm.jee.wls.EjbDeploymentDescriptor;
import org.springframework.sbm.jee.wls.WlsEjbDeploymentDescriptor;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class WlsEjbDeploymentDescriptorTest {

    @Test
    void test() {
        String wlsEjbJarXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<weblogic-ejb-jar xmlns=\"http://www.bea.com/ns/weblogic/90\" xmlns:j2ee=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bea.com/ns/weblogic/90 http://www.bea.com/ns/weblogic/90/weblogic-ejb-jar.xsd\">\n" +
                        "    <weblogic-enterprise-bean>\n" +
                        "        <ejb-name>someEjb</ejb-name>\n" +
                        "        <transaction-descriptor>\n" +
                        "            <trans-timeout-seconds>60000</trans-timeout-seconds>\n" +
                        "        </transaction-descriptor>\n" +
                        "    </weblogic-enterprise-bean>\n" +
                        "    <weblogic-enterprise-bean>\n" +
                        "        <ejb-name>someEjb2</ejb-name>\n" +
                        "        <transaction-descriptor>\n" +
                        "            <trans-timeout-seconds>5000</trans-timeout-seconds>\n" +
                        "        </transaction-descriptor>\n" +
                        "    </weblogic-enterprise-bean>\n" +
                        "</weblogic-ejb-jar>";

        WlsEjbDeploymentDescriptor wlsEjbDeploymentDescriptor = new WlsEjbDeploymentDescriptor(Path.of("projectRoot").toAbsolutePath(), Path.of("weblogic-ejb-jar.xml"), wlsEjbJarXml);

        EjbDeploymentDescriptor ejbDeploymentDescriptor = wlsEjbDeploymentDescriptor.getEjbDeploymentDescriptor();

        assertThat(ejbDeploymentDescriptor.getEnterpriseBean(0).getTransactionDescriptor().getTransactionTimeoutSeconds()).isEqualTo(60000);
        assertThat(ejbDeploymentDescriptor.getEnterpriseBean(1).getTransactionDescriptor().getTransactionTimeoutSeconds()).isEqualTo(5000);
    }
}
