package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.mapper.code.ORDER;
import com.example.demo.configs.mapper.entity.EntityColumn;
import com.example.demo.configs.mapper.utils.MetaObjectUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public class SelectKeyHelper {
    public SelectKeyHelper() {
    }

    public static void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column, Class<?> entityClass, Boolean executeBefore, String identity) {
        String keyId = ms.getId() + "!selectKey";
        if (!ms.getConfiguration().hasKeyGenerator(keyId)) {
            Configuration configuration = ms.getConfiguration();
            String IDENTITY = column.getGenerator() != null && !column.getGenerator().equals("") ? column.getGenerator() : identity;
            Object keyGenerator;
            if (IDENTITY.equalsIgnoreCase("JDBC")) {
                keyGenerator = new Jdbc3KeyGenerator();
            } else {
                SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);
                MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
                statementBuilder.resource(ms.getResource());
                statementBuilder.fetchSize((Integer)null);
                statementBuilder.statementType(StatementType.STATEMENT);
                statementBuilder.keyGenerator(new NoKeyGenerator());
                statementBuilder.keyProperty(column.getProperty());
                statementBuilder.keyColumn((String)null);
                statementBuilder.databaseId((String)null);
                statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
                statementBuilder.resultOrdered(false);
                statementBuilder.resulSets((String)null);
                statementBuilder.timeout(configuration.getDefaultStatementTimeout());
                List<ParameterMapping> parameterMappings = new ArrayList();
                org.apache.ibatis.mapping.ParameterMap.Builder inlineParameterMapBuilder = new org.apache.ibatis.mapping.ParameterMap.Builder(configuration, statementBuilder.id() + "-Inline", entityClass, parameterMappings);
                statementBuilder.parameterMap(inlineParameterMapBuilder.build());
                List<ResultMap> resultMaps = new ArrayList();
                org.apache.ibatis.mapping.ResultMap.Builder inlineResultMapBuilder = new org.apache.ibatis.mapping.ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", column.getJavaType(), new ArrayList(), (Boolean)null);
                resultMaps.add(inlineResultMapBuilder.build());
                statementBuilder.resultMaps(resultMaps);
                statementBuilder.resultSetType((ResultSetType)null);
                statementBuilder.flushCacheRequired(false);
                statementBuilder.useCache(false);
                statementBuilder.cache((Cache)null);
                MappedStatement statement = statementBuilder.build();

                try {
                    configuration.addMappedStatement(statement);
                } catch (Exception var20) {
                    ;
                }

                MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
                keyGenerator = new SelectKeyGenerator(keyStatement, column.getOrder() != ORDER.DEFAULT ? column.getOrder() == ORDER.BEFORE : executeBefore);

                try {
                    configuration.addKeyGenerator(keyId, (KeyGenerator)keyGenerator);
                } catch (Exception var19) {
                    ;
                }
            }

            try {
                MetaObject msObject = MetaObjectUtils.forObject(ms);
                msObject.setValue("keyGenerator", keyGenerator);
                msObject.setValue("keyProperties", column.getTable().getKeyProperties());
                msObject.setValue("keyColumns", column.getTable().getKeyColumns());
            } catch (Exception var18) {
                ;
            }

        }
    }
}
