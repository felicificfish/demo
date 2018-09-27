package com.example.demo.configs.mapper.spring;

import com.example.demo.configs.mapper.helper.MapperHelper;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
    private Class<T> mapperInterface;
    private boolean addToConfig = true;
    private MapperHelper mapperHelper;

    public MapperFactoryBean() {
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = this.getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception var6) {
                this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", var6);
                throw new IllegalArgumentException(var6);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        if (configuration.hasMapper(this.mapperInterface) && this.mapperHelper != null && this.mapperHelper.isExtendCommonMapper(this.mapperInterface)) {
            this.mapperHelper.processConfiguration(this.getSqlSession().getConfiguration(), this.mapperInterface);
        }

    }

    public Class<T> getMapperInterface() {
        return this.mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T getObject() throws Exception {
        return this.getSqlSession().getMapper(this.mapperInterface);
    }

    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    public boolean isAddToConfig() {
        return this.addToConfig;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public boolean isSingleton() {
        return true;
    }
}

