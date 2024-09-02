package com.vonage.kibana_crawler.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(150000); // Connection timeout for 2 minutes and 30 seconds
        requestFactory.setConnectionRequestTimeout(30000); // Connection timeout for 30 seconds
        return new RestTemplate();
    }
}
