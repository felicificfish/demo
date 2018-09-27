package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.mapper.entity.Config;
import com.example.demo.configs.mapper.entity.EntityTable;

public interface EntityResolve {
    EntityTable resolveEntity(Class<?> entityClass, Config config);
}
