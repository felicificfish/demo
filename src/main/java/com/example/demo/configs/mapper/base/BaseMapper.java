package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BaseMapper<T> extends BaseSelectMapper<T>, BaseDeleteMapper<T>, ExampleMapper<T> {
}