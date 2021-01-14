package com.katas.lookupservice.config;

import com.katas.lookupservice.interceptor.SmsNotificationInterceptor;
import com.katas.lookupservice.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    private final LookupService lookupService;

    @Autowired
    public RestTemplateConfig(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean("smsRestTemplate")
    public RestTemplate smsRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new SmsNotificationInterceptor(lookupService)));
        return restTemplate;
    }
}
