package com.vmware.example.sccsclientconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
@RestController
public class SccsClientConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SccsClientConfigApplication.class, args);
    }

    @GetMapping("/test")
    public String test() {
        return "yes, I am here...";
    }
}
