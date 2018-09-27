package com.example.demo.configs.mapper.provider;

import com.example.demo.configs.mapper.helper.MapperHelper;
import com.example.demo.configs.mapper.helper.MapperTemplate;
import com.example.demo.configs.mapper.helper.SqlHelper;
import org.apache.ibatis.mapping.MappedStatement;

public class ExampleProvider extends MapperTemplate {
    public ExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectCountByExample(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }

        sql.append(SqlHelper.exampleCountColumn(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    public String selectByExample(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }

        sql.append("<if test=\"distinct\">distinct</if>");
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }
}

