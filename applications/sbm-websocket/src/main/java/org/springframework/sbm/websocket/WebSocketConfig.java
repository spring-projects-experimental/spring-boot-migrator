package org.springframework.sbm.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Profile("websocket")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
    messageConverters.add(new MappingJackson2MessageConverter());
    return true;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/sbm")
            .setPreservePublishOrder(true)
            .enableSimpleBroker("/topic", "/queue");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/endpoint").setAllowedOrigins("http://localhost:5000");
  }

  @Bean
  public StompSessionHandler stompSessionHandler() {
    return new ServerStompSessionHandler();
  }

}