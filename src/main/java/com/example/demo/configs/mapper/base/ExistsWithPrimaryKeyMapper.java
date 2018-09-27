package com.example.demo.configs.mapper.base;


import com.example.demo.configs.mapper.annotation.RegisterMapper;
import com.example.demo.configs.mapper.provider.BaseSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;

@RegisterMapper
public interface ExistsWithPrimaryKeyMapper<T> {
    @SelectProvider(
            type = BaseSelectProvider.class,
            method = "dynamicSQL"
    )
    boolean existsWithPrimaryKey(Object key);
}
