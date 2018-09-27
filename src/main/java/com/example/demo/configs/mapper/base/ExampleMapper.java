package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface ExampleMapper<T> extends SelectByExampleMapper<T>, SelectCountByExampleMapper<T> {
}
