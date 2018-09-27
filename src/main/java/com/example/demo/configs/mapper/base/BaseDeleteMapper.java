package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BaseDeleteMapper<T> extends DeleteMapper<T>, DeleteByPrimaryKeyMapper<T> {
}
