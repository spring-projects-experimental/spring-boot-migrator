package com.example.demorabitmqspringintegration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class JavaAmqp {
//    @Bean
//    public MessageChannel amqpInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer,
//                                             @Qualifier("amqpInputChannel") MessageChannel channel) {
//        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
//        adapter.setOutputChannel(channel);
//        return adapter;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container =
//                new SimpleMessageListenerContainer(connectionFactory);
//        container.setQueueNames("FirstQueue");
//        container.setConcurrentConsumers(1);
//        return container;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "amqpInputChannel")
//    public MessageHandler handler() {
//        return m -> System.out.println(m.getPayload());
//    }
}
