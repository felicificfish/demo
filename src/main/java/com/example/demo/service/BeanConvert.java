package com.example.demo.service;

public interface BeanConvert<S, T> {
    T convert(S s);
}
