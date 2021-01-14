package com.katas.lookupservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsNotificationService {

    private final LookupService lookupService;
    private final RestTemplate restTemplate;

    @Autowired
    public SmsNotificationService(LookupService lookupService, @Qualifier(value = "smsRestTemplate") RestTemplate restTemplate) {
        this.lookupService = lookupService;
        this.restTemplate = restTemplate;
    }

}
