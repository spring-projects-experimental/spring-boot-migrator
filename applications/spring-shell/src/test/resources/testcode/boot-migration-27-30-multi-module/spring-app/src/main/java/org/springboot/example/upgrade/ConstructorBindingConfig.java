package org.springboot.example.upgrade;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "mail")
@ConstructorBinding
public class ConstructorBindingConfig {
    private String hostName;

    public ConstructorBindingConfig(String hostName) {
        this.hostName = hostName;
    }
}
