package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.mapper.annotation.ColumnType;
import com.example.demo.configs.mapper.annotation.NameStyle;
import com.example.demo.configs.mapper.code.Style;
import com.example.demo.configs.mapper.entity.Config;
import com.example.demo.configs.mapper.entity.EntityColumn;
import com.example.demo.configs.mapper.entity.EntityField;
import com.example.demo.configs.mapper.entity.EntityTable;

import javax.persistence.*;

import com.example.demo.configs.mapper.utils.SimpleTypeUtils;
import com.example.demo.configs.mapper.utils.SqlReservedWords;
import com.example.demo.utils.StringTool;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class DefaultEntityResolve implements EntityResolve {
    private final Log log = LogFactory.getLog(DefaultEntityResolve.class);

    public DefaultEntityResolve() {
    }

    public EntityTable resolveEntity(Class<?> entityClass, Config config) {
        Style style = config.getStyle();
        if (entityClass.isAnnotationPresent(NameStyle.class)) {
            NameStyle nameStyle = (NameStyle) entityClass.getAnnotation(NameStyle.class);
            style = nameStyle.value();
        }

        EntityTable entityTable = null;
        Table fields;
        if (entityClass.isAnnotationPresent(Table.class)) {
            fields = (Table) entityClass.getAnnotation(Table.class);
            if (!"".equals(fields.name())) {
                entityTable = new EntityTable(entityClass);
                entityTable.setTable(fields);
            }
        }

        if (entityTable == null) {
            entityTable = new EntityTable(entityClass);
            String tableName = StringTool.convertByStyle(entityClass.getSimpleName(), style);
            if (!StringUtils.isEmpty(config.getWrapKeyword()) && SqlReservedWords.containsWord(tableName)) {
                tableName = MessageFormat.format(config.getWrapKeyword(), tableName);
            }

            entityTable.setName(tableName);
        }

        entityTable.setEntityClassColumns(new LinkedHashSet());
        entityTable.setEntityClassPKColumns(new LinkedHashSet());
        fields = null;
        List fieldLists;
        if (config.isEnableMethodAnnotation()) {
            fieldLists = FieldHelper.getAll(entityClass);
        } else {
            fieldLists = FieldHelper.getFields(entityClass);
        }

        Iterator var6 = fieldLists.iterator();

        while (true) {
            EntityField field;
            do {
                if (!var6.hasNext()) {
                    if (entityTable.getEntityClassPKColumns().size() == 0) {
                        entityTable.setEntityClassPKColumns(entityTable.getEntityClassColumns());
                    }

                    entityTable.initPropertyMap();
                    return entityTable;
                }

                field = (EntityField) var6.next();
            }
            while (config.isUseSimpleType() && !field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(ColumnType.class) && !SimpleTypeUtils.isSimpleType(field.getJavaType()) && (!config.isEnumAsSimpleType() || !Enum.class.isAssignableFrom(field.getJavaType())));

            this.processField(entityTable, field, config, style);
        }
    }

    protected void processField(EntityTable entityTable, EntityField field, Config config, Style style) {
        if (!field.isAnnotationPresent(Transient.class)) {
            EntityColumn entityColumn = new EntityColumn(entityTable);
            entityColumn.setUseJavaType(config.isUseJavaType());
            entityColumn.setEntityField(field);
            if (field.isAnnotationPresent(Id.class)) {
                entityColumn.setId(true);
            }

            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = (Column) field.getAnnotation(Column.class);
                columnName = column.name();
                entityColumn.setUpdatable(column.updatable());
                entityColumn.setInsertable(column.insertable());
            }

            if (field.isAnnotationPresent(ColumnType.class)) {
                ColumnType columnType = (ColumnType) field.getAnnotation(ColumnType.class);
                entityColumn.setBlob(columnType.isBlob());
                if (StringUtils.isEmpty(columnName) && !StringUtils.isEmpty(columnType.column())) {
                    columnName = columnType.column();
                }

                if (columnType.jdbcType() != JdbcType.UNDEFINED) {
                    entityColumn.setJdbcType(columnType.jdbcType());
                }

                if (columnType.typeHandler() != UnknownTypeHandler.class) {
                    entityColumn.setTypeHandler(columnType.typeHandler());
                }
            }

            if (StringUtils.isEmpty(columnName)) {
                columnName = StringTool.convertByStyle(field.getName(), style);
            }

            if (!StringUtils.isEmpty(config.getWrapKeyword()) && SqlReservedWords.containsWord(columnName)) {
                columnName = MessageFormat.format(config.getWrapKeyword(), columnName);
            }

            entityColumn.setProperty(field.getName());
            entityColumn.setColumn(columnName);
            entityColumn.setJavaType(field.getJavaType());
            if (field.getJavaType().isPrimitive()) {
                this.log.warn("通用 Mapper 警告信息: <[" + entityColumn + "]> 使用了基本类型，基本类型在动态 SQL 中由于存在默认值，因此任何时候都不等于 null，建议修改基本类型为对应的包装类型!");
            }

            this.processOrderBy(entityTable, field, entityColumn);
            entityTable.getEntityClassColumns().add(entityColumn);
            if (entityColumn.isId()) {
                entityTable.getEntityClassPKColumns().add(entityColumn);
            }

        }
    }

    protected void processOrderBy(EntityTable entityTable, EntityField field, EntityColumn entityColumn) {
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = (OrderBy) field.getAnnotation(OrderBy.class);
            if ("".equals(orderBy.value())) {
                entityColumn.setOrderBy("ASC");
            } else {
                entityColumn.setOrderBy(orderBy.value());
            }
        }

    }
}
