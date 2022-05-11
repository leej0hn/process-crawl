package com.redscarf.process.crawl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author LeeJohn
 * @Date 2020-12-12 11:03
 */
@Slf4j
@SpringBootApplication
public class CrawlServer implements CommandLineRunner {
    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        new SpringApplication(CrawlServer.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("{} 启动成功", this.appName);
    }
}
