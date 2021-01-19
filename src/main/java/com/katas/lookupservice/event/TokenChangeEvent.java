package com.katas.lookupservice.event;

import org.springframework.context.ApplicationEvent;

public class TokenChangeEvent extends ApplicationEvent {

    private final String token;

    public TokenChangeEvent(String token) {
        super(token);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
