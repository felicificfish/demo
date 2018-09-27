package com.example.demo.configs.mapper.provider;


import com.example.demo.configs.mapper.helper.MapperHelper;
import com.example.demo.configs.mapper.helper.MapperTemplate;

public class EmptyProvider extends MapperTemplate {
    public EmptyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public boolean supportMethod(String msId) {
        return false;
    }
}
