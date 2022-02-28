package com.example.demorabitmqspringintegration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleContextAmqp {
    private static final Log LOGGER = LogFactory.getLog(SimpleContextAmqp.class);

    private SimpleContextAmqp() {
    }

    /**
     * Load the Spring Integration Application Context
     *
     * @param args - command line arguments
     */
    public static void main(final String... args) {

        LOGGER.info("\n========================================================="
                + "\n                                                         "
                + "\n          Welcome to Spring Integration!                 "
                + "\n                                                         "
                + "\n    For more information please visit:                   "
                + "\n    https://www.springsource.org/spring-integration       "
                + "\n                                                         "
                + "\n=========================================================");

        @SuppressWarnings("resource") final AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/spring-integration-context.xml");

        context.registerShutdownHook();

        LOGGER.info("\n========================================================="
                + "\n                                                          "
                + "\n    This is the AMQP Sample -                             "
                + "\n                                                          "
                + "\n    Please enter some text and press return. The entered  "
                + "\n    Message will be sent to the configured RabbitMQ Queue,"
                + "\n    then again immediately retrieved from the Message     "
                + "\n    Broker and ultimately printed to the command line.    "
                + "\n                                                          "
                + "\n=========================================================");

    }
}
