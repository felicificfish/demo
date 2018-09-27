package com.example.demo.configs.exception;

import com.example.demo.utils.SpringContext;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class SweetException extends RuntimeException {
    private String exCode = "5500";
    private Object[] arguments;

    public SweetException(String excode) {
        if (StringUtils.hasText(excode)) {
            this.exCode = excode;
        }

    }

    public SweetException(String excode, Object[] arguments) {
        if (StringUtils.hasText(excode)) {
            this.exCode = excode;
        }

        this.arguments = arguments;
    }

    public SweetException(String excode, String msg) {
        super(msg);
        if (StringUtils.hasText(excode)) {
            this.exCode = excode;
        }

    }

    public SweetException(String excode, String msg, Object[] arguments) {
        super(msg);
        if (StringUtils.hasText(excode)) {
            this.exCode = excode;
        }

        this.arguments = arguments;
    }

    public SweetException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getI18nMessage() {
        Locale locale = Locale.CHINA;
        String i18nMessage = ((MessageSource) SpringContext.getBean(MessageSource.class)).getMessage(this.exCode, this.arguments, super.getMessage(), locale);
        return StringUtils.hasText(i18nMessage) ? i18nMessage : super.getMessage();
    }

    public String getExCode() {
        return this.exCode;
    }

    public Object[] getArguments() {
        return this.arguments;
    }
}
