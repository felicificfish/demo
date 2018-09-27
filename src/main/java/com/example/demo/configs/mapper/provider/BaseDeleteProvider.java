package com.example.demo.configs.mapper.provider;

import com.example.demo.configs.mapper.helper.EntityHelper;
import com.example.demo.configs.mapper.helper.MapperHelper;
import com.example.demo.configs.mapper.helper.MapperTemplate;
import com.example.demo.configs.mapper.helper.SqlHelper;
import org.apache.ibatis.mapping.MappedStatement;

public class BaseDeleteProvider extends MapperTemplate {
    public BaseDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String delete(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (this.getConfig().isSafeDelete()) {
            sql.append(SqlHelper.notAllNullParameterCheck("_parameter", EntityHelper.getColumns(entityClass)));
        }

        sql.append(SqlHelper.deleteFromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()));
        return sql.toString();
    }

    public String deleteByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }
}
