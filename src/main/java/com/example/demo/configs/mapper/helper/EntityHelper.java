package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Config;
import com.example.demo.configs.mapper.entity.EntityColumn;
import com.example.demo.configs.mapper.entity.EntityTable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EntityHelper {
    private static final Map<Class<?>, EntityTable> entityTableMap = new ConcurrentHashMap();
    private static final EntityResolve DEFAULT = new DefaultEntityResolve();
    private static EntityResolve resolve;

    public EntityHelper() {
    }

    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = (EntityTable) entityTableMap.get(entityClass);
        if (entityTable == null) {
            throw new ValidateException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        } else {
            return entityTable;
        }
    }

    public static String getOrderByClause(Class<?> entityClass) {
        EntityTable table = getEntityTable(entityClass);
        if (table.getOrderByClause() != null) {
            return table.getOrderByClause();
        } else {
            StringBuilder orderBy = new StringBuilder();
            Iterator var3 = table.getEntityClassColumns().iterator();

            while (var3.hasNext()) {
                EntityColumn column = (EntityColumn) var3.next();
                if (column.getOrderBy() != null) {
                    if (orderBy.length() != 0) {
                        orderBy.append(",");
                    }

                    orderBy.append(column.getColumn()).append(" ").append(column.getOrderBy());
                }
            }

            table.setOrderByClause(orderBy.toString());
            return table.getOrderByClause();
        }
    }

    public static Set<EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    public static Set<EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    public static String getSelectColumns(Class<?> entityClass) {
        EntityTable entityTable = getEntityTable(entityClass);
        if (entityTable.getBaseSelect() != null) {
            return entityTable.getBaseSelect();
        } else {
            Set<EntityColumn> columnList = getColumns(entityClass);
            StringBuilder selectBuilder = new StringBuilder();
            boolean skipAlias = Map.class.isAssignableFrom(entityClass);
            Iterator var5 = columnList.iterator();

            while (true) {
                while (var5.hasNext()) {
                    EntityColumn entityColumn = (EntityColumn) var5.next();
                    selectBuilder.append(entityColumn.getColumn());
                    if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                        if (entityColumn.getColumn().substring(1, entityColumn.getColumn().length() - 1).equalsIgnoreCase(entityColumn.getProperty())) {
                            selectBuilder.append(",");
                        } else {
                            selectBuilder.append(" AS ").append(entityColumn.getProperty()).append(",");
                        }
                    } else {
                        selectBuilder.append(",");
                    }
                }

                entityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
                return entityTable.getBaseSelect();
            }
        }
    }

    public static synchronized void initEntityNameMap(Class<?> entityClass, Config config) {
        if (entityTableMap.get(entityClass) == null) {
            EntityTable entityTable = resolve.resolveEntity(entityClass, config);
            entityTableMap.put(entityClass, entityTable);
        }
    }

    static void setResolve(EntityResolve entityResolve) {
        resolve = entityResolve;
    }

    static {
        resolve = DEFAULT;
    }
}

