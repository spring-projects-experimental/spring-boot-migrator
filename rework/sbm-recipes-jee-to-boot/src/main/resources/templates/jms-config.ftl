<#if packageName?has_content>
package ${packageName};

</#if>
import javax.jms.ConnectionFactory;

<#if queues?has_content>
import javax.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import javax.jms.JMSException;
</#if>

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

@Configuration
@EnableJms
public class ${className} {

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
           ConnectionFactory connectionFactory,
           DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

<#list queues as key, value>
    @Bean
    Queue ${key}(ConnectionFactory connectionFactory) throws JMSException {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(${value});
        return activeMQQueue;
    }

</#list>
}
