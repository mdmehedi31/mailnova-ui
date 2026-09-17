package com.mailnova.restEndPointCaller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestCallerConfig {

    @Value("${mailnova.service.url}")
    private String serviceUrl;


    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient createRestClient(RestClient.Builder builder) {
        System.out.println("Creating RestClient "+serviceUrl);
        return builder.baseUrl(serviceUrl).build();
    }
}
