package com.example.demorabitmqspringintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
//@ImportResource("classpath:spring-integration-context.xml")
public class DemoRabitmqSpringIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRabitmqSpringIntegrationApplication.class, args);
    }

}
