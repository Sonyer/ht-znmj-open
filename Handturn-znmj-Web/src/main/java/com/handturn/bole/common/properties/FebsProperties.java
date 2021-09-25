package com.handturn.bole.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Eric
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:bole.properties"})
@ConfigurationProperties(prefix = "bole")
public class FebsProperties {

    private ShiroProperties shiro = new ShiroProperties();
    private boolean autoOpenBrowser = true;
    private String[] autoOpenBrowserEnv = {};
    private SwaggerProperties swagger = new SwaggerProperties();
}
