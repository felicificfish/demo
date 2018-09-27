package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.provider.BaseSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;

@RegisterMapper
public interface SelectOneMapper<T> {
    @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
    T selectOne(T record);
}
