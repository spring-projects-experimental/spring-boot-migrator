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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class EjbDeploymentDescriptor {
    @Getter
    private final List<EnterpriseBean> enterpriseBeans = new ArrayList<>();

    public int addEnterpriseBean(EnterpriseBean enterpriseBean) {
        this.enterpriseBeans.add(enterpriseBean);
        return enterpriseBeans.indexOf(enterpriseBean);
    }

    public EnterpriseBean getEnterpriseBean(int currentIndex) {
        return enterpriseBeans.get(currentIndex);
    }

    @ToString
    @Getter
    @Setter
    public static class EnterpriseBean {
        private String ejbName;
        private TransactionDescriptor transactionDescriptor;

        public TransactionDescriptor addTransactionDescriptor() {
            return this.transactionDescriptor = new TransactionDescriptor();
        }

        @Getter
        @Setter
        @ToString
        public static class TransactionDescriptor {
            private Integer transactionTimeoutSeconds;
        }
    }
}