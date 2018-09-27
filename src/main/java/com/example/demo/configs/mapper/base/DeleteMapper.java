package com.example.demo.configs.mapper.base;


import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.provider.BaseDeleteProvider;
import org.apache.ibatis.annotations.DeleteProvider;

@RegisterMapper
public interface DeleteMapper<T> {
    @DeleteProvider(
            type = BaseDeleteProvider.class,
            method = "dynamicSQL"
    )
    int delete(T record);
}