package com.katas.lookupservice.exception;

import java.util.Locale;

public class RequestException extends RuntimeException {
    public RequestException(String tag, String message) {
        super(String.format(Locale.getDefault(), "%1s %2s", tag, message));
    }
}
