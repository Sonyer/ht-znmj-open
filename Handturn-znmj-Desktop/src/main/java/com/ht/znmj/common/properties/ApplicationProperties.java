package com.ht.znmj.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用信息
 */
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "application-info")
public class ApplicationProperties {
    private String version = "0.0.0.1";
    private String bit = "32";
    private String cloudFlag = "0";
}
