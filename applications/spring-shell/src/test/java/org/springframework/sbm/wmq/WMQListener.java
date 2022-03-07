package org.springframework.sbm.wmq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.function.Consumer;

public class WMQListener {

    public void listenForMessage(int port, String queueName, final Consumer<String> messageConsumer) throws JMSException {
        WMQFactory wmqFactory = new WMQFactory();
        ConnectionFactory cf = wmqFactory.createFactory(port);

        // Create JMS objects
        JMSContext context = cf.createContext();
        Destination destination = context.createQueue("queue:///" + queueName);

        // autoclosable
        JMSConsumer consumer = context.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    messageConsumer.accept(message.getBody(String.class));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    } // end main()
}