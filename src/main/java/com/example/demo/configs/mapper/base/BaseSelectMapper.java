package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BaseSelectMapper<T> extends SelectOneMapper<T>, SelectMapper<T>, SelectAllMapper<T>, SelectCountMapper<T>, SelectByPrimaryKeyMapper<T>, ExistsWithPrimaryKeyMapper<T> {
}
