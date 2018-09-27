package com.example.demo.configs.mapper.annotation;

import com.example.demo.configs.mapper.code.Style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameStyle {
    Style value() default Style.NORMAL;
}
