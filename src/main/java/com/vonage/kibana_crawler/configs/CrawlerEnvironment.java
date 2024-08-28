package com.vonage.kibana_crawler.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crawler")
@Getter
@Setter
public class CrawlerEnvironment {

    @Value("${crawler.kibana.api.client.retry:3}")
    private Integer kibanaApiClientRetry;

    @Value("${crawler.blocking_queue.pool.size:20}")
    private Integer blockingQueuePoolSize;
}
