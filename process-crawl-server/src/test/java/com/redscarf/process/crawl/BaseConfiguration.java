package com.redscarf.process.crawl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * <p>function:
 * <p>User: LeeJohn
 * <p>Date: 2017/12/01
 * <p>Version: 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {"com.redscarf.process.crawl"}
)
public class BaseConfiguration {
}
