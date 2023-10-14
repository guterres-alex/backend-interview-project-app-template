package com.ninjaone.backendinterviewproject.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.US);
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.US);
    }
}
