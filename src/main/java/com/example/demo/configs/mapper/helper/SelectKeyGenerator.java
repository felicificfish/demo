package com.example.demo.configs.mapper.helper;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;
import java.util.List;

public class SelectKeyGenerator implements KeyGenerator {
    public static final String SELECT_KEY_SUFFIX = "!selectKey";
    private boolean executeBefore;
    private MappedStatement keyStatement;

    public SelectKeyGenerator(MappedStatement keyStatement, boolean executeBefore) {
        this.executeBefore = executeBefore;
        this.keyStatement = keyStatement;
    }

    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        if (this.executeBefore) {
            this.processGeneratedKeys(executor, ms, parameter);
        }

    }

    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        if (!this.executeBefore) {
            this.processGeneratedKeys(executor, ms, parameter);
        }

    }

    private void processGeneratedKeys(Executor executor, MappedStatement ms, Object parameter) {
        try {
            if (parameter != null && this.keyStatement != null && this.keyStatement.getKeyProperties() != null) {
                String[] keyProperties = this.keyStatement.getKeyProperties();
                Configuration configuration = ms.getConfiguration();
                MetaObject metaParam = configuration.newMetaObject(parameter);
                if (keyProperties != null) {
                    Executor keyExecutor = configuration.newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
                    List<Object> values = keyExecutor.query(this.keyStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
                    if (values.size() == 0) {
                        throw new ExecutorException("SelectKey returned no data.");
                    }

                    if (values.size() > 1) {
                        throw new ExecutorException("SelectKey returned more than one value.");
                    }

                    MetaObject metaResult = configuration.newMetaObject(values.get(0));
                    if (keyProperties.length == 1) {
                        if (metaResult.hasGetter(keyProperties[0])) {
                            this.setValue(metaParam, keyProperties[0], metaResult.getValue(keyProperties[0]));
                        } else {
                            this.setValue(metaParam, keyProperties[0], values.get(0));
                        }
                    } else {
                        this.handleMultipleProperties(keyProperties, metaParam, metaResult);
                    }
                }
            }

        } catch (ExecutorException var10) {
            throw var10;
        } catch (Exception var11) {
            throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + var11, var11);
        }
    }

    private void handleMultipleProperties(String[] keyProperties, MetaObject metaParam, MetaObject metaResult) {
        String[] keyColumns = this.keyStatement.getKeyColumns();
        if (keyColumns != null && keyColumns.length != 0) {
            if (keyColumns.length != keyProperties.length) {
                throw new ExecutorException("If SelectKey has key columns, the number must match the number of key properties.");
            }

            for (int i = 0; i < keyProperties.length; ++i) {
                this.setValue(metaParam, keyProperties[i], metaResult.getValue(keyColumns[i]));
            }
        } else {
            String[] var5 = keyProperties;
            int var6 = keyProperties.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String keyProperty = var5[var7];
                this.setValue(metaParam, keyProperty, metaResult.getValue(keyProperty));
            }
        }

    }

    private void setValue(MetaObject metaParam, String property, Object value) {
        if (metaParam.hasSetter(property)) {
            if (metaParam.hasGetter(property)) {
                Object defaultValue = metaParam.getValue(property);
                if (defaultValue != null) {
                    return;
                }
            }

            metaParam.setValue(property, value);
        } else {
            throw new ExecutorException("No setter found for the keyProperty '" + property + "' in " + metaParam.getOriginalObject().getClass().getName() + ".");
        }
    }
}

