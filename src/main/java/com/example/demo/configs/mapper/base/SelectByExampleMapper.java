package com.example.demo.configs.mapper.base;

import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.provider.ExampleProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@RegisterMapper
public interface SelectByExampleMapper<T> {
    @SelectProvider(
            type = ExampleProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectByExample(Object example);
}
