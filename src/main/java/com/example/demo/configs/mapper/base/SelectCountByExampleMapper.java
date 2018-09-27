package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.provider.ExampleProvider;
import org.apache.ibatis.annotations.SelectProvider;

@RegisterMapper
public interface SelectCountByExampleMapper<T> {
    @SelectProvider(
            type = ExampleProvider.class,
            method = "dynamicSQL"
    )
    int selectCountByExample(Object example);
}