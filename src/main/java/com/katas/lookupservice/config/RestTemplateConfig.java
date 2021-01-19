package com.katas.lookupservice.config;

import com.katas.lookupservice.interceptor.SmsNotificationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("smsRestTemplate")
    public RestTemplate smsRestTemplate(SmsNotificationInterceptor notificationInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(notificationInterceptor));
        return restTemplate;
    }
}
