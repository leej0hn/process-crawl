package com.redscarf.process.crawl.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: leejohn
 * @date: 2022/2/16 14:31
 * @since:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "processon.userinfo")
public class ProcessOnUserInfoProperties {
    private String uk;
    private String fullName;
    private String cookie;
}
