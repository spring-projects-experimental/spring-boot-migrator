package org.springboot.example.upgrade;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "region")
@ConstructorBinding
public class ConstructorBindingConfig {
    private String regionCode;

    public ConstructorBindingConfig(String regionCode) {
        this.regionCode = regionCode;
    }
}
