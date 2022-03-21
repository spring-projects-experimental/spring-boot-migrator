package com.vmware.sbm.example.sccsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SccsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SccsClientApplication.class, args);
    }

    @Value("${property1}")
    private String property1;

    @Value("${property2}")
    private String property2;

    @GetMapping(path = "/config1")
    public String getConfig1() {
        return property1;
    }

    @GetMapping(path = "/config2")
    public String getConfig2() {
        return property2;
    }
}
