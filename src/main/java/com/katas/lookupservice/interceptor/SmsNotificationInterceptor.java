package com.katas.lookupservice.interceptor;

import com.katas.lookupservice.service.LookupService;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class SmsNotificationInterceptor implements ClientHttpRequestInterceptor {

    private final LookupService service;
    public SmsNotificationInterceptor(LookupService service) {
        this.service = service;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //TODO do something
        return null;
    }
}
