package org.springboot.example.upgrade;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "region")
public class RegionConfig {
    private String regionCode;

    public RegionConfig(String regionCode) {
        this.regionCode = regionCode;
    }
}
