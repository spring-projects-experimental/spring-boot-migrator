package com.example.demorabitmqspringintegration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dispatcher.LoadBalancingStrategy;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class JavaDSLAmqp {
    @Bean
    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {
        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "FirstQueue"))
                .transform(new ObjectToStringTransformer())
                .transform(s -> "SI Java DSL: " + s)
                .log()
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("si.out.queue"))
                .get();
    }
}
