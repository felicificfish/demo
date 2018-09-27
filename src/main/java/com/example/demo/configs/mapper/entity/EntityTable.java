package com.example.demo.configs.mapper.entity;

import com.example.demo.configs.exception.ValidateException;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityTable {
    public static final Pattern DELIMITER = Pattern.compile("^[`\\[\"]?(.*?)[`\\]\"]?$");
    protected Map<String, EntityColumn> propertyMap;
    private String name;
    private String catalog;
    private String schema;
    private String orderByClause;
    private String baseSelect;
    private Set<EntityColumn> entityClassColumns;
    private Set<EntityColumn> entityClassPKColumns;
    private List<String> keyProperties;
    private List<String> keyColumns;
    private ResultMap resultMap;
    private Class<?> entityClass;

    public EntityTable(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public ResultMap getResultMap(Configuration configuration) {
        if (this.resultMap != null) {
            return this.resultMap;
        } else if (this.entityClassColumns != null && this.entityClassColumns.size() != 0) {
            List<ResultMapping> resultMappings = new ArrayList();
            Iterator var3 = this.entityClassColumns.iterator();

            while (var3.hasNext()) {
                EntityColumn entityColumn = (EntityColumn) var3.next();
                String column = entityColumn.getColumn();
                Matcher matcher = DELIMITER.matcher(column);
                if (matcher.find()) {
                    column = matcher.group(1);
                }

                ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), column, entityColumn.getJavaType());
                if (entityColumn.getJdbcType() != null) {
                    builder.jdbcType(entityColumn.getJdbcType());
                }

                if (entityColumn.getTypeHandler() != null) {
                    try {
                        builder.typeHandler(this.getInstance(entityColumn.getJavaType(), entityColumn.getTypeHandler()));
                    } catch (Exception var9) {
                        throw new ValidateException(var9.getMessage());
                    }
                }

                List<ResultFlag> flags = new ArrayList();
                if (entityColumn.isId()) {
                    flags.add(ResultFlag.ID);
                }

                builder.flags(flags);
                resultMappings.add(builder.build());
            }

            org.apache.ibatis.mapping.ResultMap.Builder builder = new org.apache.ibatis.mapping.ResultMap.Builder(configuration, "BaseMapperResultMap", this.entityClass, resultMappings, true);
            this.resultMap = builder.build();
            return this.resultMap;
        } else {
            return null;
        }
    }

    public void initPropertyMap() {
        this.propertyMap = new HashMap(this.getEntityClassColumns().size());
        Iterator var1 = this.getEntityClassColumns().iterator();

        while (var1.hasNext()) {
            EntityColumn column = (EntityColumn) var1.next();
            this.propertyMap.put(column.getProperty(), column);
        }

    }

    public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        Constructor c;
        if (javaTypeClass != null) {
            try {
                c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler) c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException var5) {
                ;
            } catch (Exception var6) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, var6);
            }
        }

        try {
            c = typeHandlerClass.getConstructor();
            return (TypeHandler) c.newInstance();
        } catch (Exception var4) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, var4);
        }
    }

    public String getBaseSelect() {
        return this.baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public Set<EntityColumn> getEntityClassColumns() {
        return this.entityClassColumns;
    }

    public void setEntityClassColumns(Set<EntityColumn> entityClassColumns) {
        this.entityClassColumns = entityClassColumns;
    }

    public Set<EntityColumn> getEntityClassPKColumns() {
        return this.entityClassPKColumns;
    }

    public void setEntityClassPKColumns(Set<EntityColumn> entityClassPKColumns) {
        this.entityClassPKColumns = entityClassPKColumns;
    }

    public String[] getKeyColumns() {
        return this.keyColumns != null && this.keyColumns.size() > 0 ? (String[]) this.keyColumns.toArray(new String[0]) : new String[0];
    }

    public void setKeyColumns(String keyColumn) {
        if (this.keyColumns == null) {
            this.keyColumns = new ArrayList();
            this.keyColumns.add(keyColumn);
        } else {
            this.keyColumns.add(keyColumn);
        }

    }

    public String[] getKeyProperties() {
        return this.keyProperties != null && this.keyProperties.size() > 0 ? (String[]) this.keyProperties.toArray(new String[0]) : new String[0];
    }

    public void setKeyProperties(String keyProperty) {
        if (this.keyProperties == null) {
            this.keyProperties = new ArrayList();
            this.keyProperties.add(keyProperty);
        } else {
            this.keyProperties.add(keyProperty);
        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getPrefix() {
        if (!StringUtils.isEmpty(this.catalog)) {
            return this.catalog;
        } else {
            return !StringUtils.isEmpty(this.schema) ? this.schema : "";
        }
    }

    public Map<String, EntityColumn> getPropertyMap() {
        return this.propertyMap;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setKeyColumns(List<String> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public void setKeyProperties(List<String> keyProperties) {
        this.keyProperties = keyProperties;
    }

    public void setTable(Table table) {
        this.name = table.name();
        this.catalog = table.catalog();
        this.schema = table.schema();
    }
}
