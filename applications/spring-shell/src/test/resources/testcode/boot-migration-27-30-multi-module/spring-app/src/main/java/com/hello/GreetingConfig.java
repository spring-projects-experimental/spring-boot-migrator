package com.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GreetingConfig {

    @Bean
    public String hello() {
        return "こんにちは";
    }
}
