package com.katas.lookupservice.interceptor;

import com.katas.lookupservice.event.TokenChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class SmsNotificationInterceptor implements ClientHttpRequestInterceptor {
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public SmsNotificationInterceptor(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //...stuffs

        //when obtain the new token the publish an event to update the database
        eventPublisher.publishEvent(new TokenChangeEvent(UUID.randomUUID().toString()));

        //...stuffs
        return null;
    }


}
