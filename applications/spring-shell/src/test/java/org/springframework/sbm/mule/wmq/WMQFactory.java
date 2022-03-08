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
package org.springframework.sbm.mule.wmq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class WMQFactory {
    private static final String HOST = "localhost"; // Host name or IP address
    private static final String CHANNEL = "DEV.APP.SVRCONN"; // Channel name
    private static final String QMGR = "QM1"; // Queue manager name
    private static final String APP_USER = "app"; // User name that application uses to connect to MQ
    private static final String APP_PASSWORD = "passw0rd"; // Password that the application uses to connect to MQ

    private final JmsFactoryFactory ff;

    public WMQFactory() throws JMSException {
        this.ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);;
    }

    ConnectionFactory createFactory(int port) throws JMSException {
        JmsConnectionFactory cf = ff.createConnectionFactory();

        // Set the properties
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
        cf.setIntProperty(WMQConstants.WMQ_PORT, port);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
        cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
        cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
        cf.setStringProperty(WMQConstants.USERID, APP_USER);
        cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);

        return cf;
    }
}
