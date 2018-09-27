package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.mapper.entity.EntityColumn;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Set;

public class SqlHelper {
    public SqlHelper() {
    }

    public static String getIfNotNull(EntityColumn column, String contents, boolean empty) {
        return getIfNotNull((String) null, column, contents, empty);
    }

    public static String getIfNotNull(String entityName, EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (!StringUtils.isEmpty(entityName)) {
            sql.append(entityName).append(".");
        }

        sql.append(column.getProperty()).append(" != null");
        if (empty && column.getJavaType().equals(String.class)) {
            sql.append(" and ");
            if (!StringUtils.isEmpty(entityName)) {
                sql.append(entityName).append(".");
            }

            sql.append(column.getProperty()).append(" != '' ");
        }

        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getIfIsNull(String entityName, EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (!StringUtils.isEmpty(entityName)) {
            sql.append(entityName).append(".");
        }

        sql.append(column.getProperty()).append(" == null");
        if (empty && column.getJavaType().equals(String.class)) {
            sql.append(" or ");
            if (!StringUtils.isEmpty(entityName)) {
                sql.append(entityName).append(".");
            }

            sql.append(column.getProperty()).append(" == '' ");
        }

        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        Iterator var3 = columnSet.iterator();

        while (var3.hasNext()) {
            EntityColumn entityColumn = (EntityColumn) var3.next();
            sql.append(entityColumn.getColumn()).append(",");
        }

        return sql.substring(0, sql.length() - 1);
    }

    public static String selectAllColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(getAllColumns(entityClass));
        sql.append(" ");
        return sql.toString();
    }

    public static String selectCount(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(((EntityColumn) pkColumns.iterator().next()).getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }

        return sql.toString();
    }

    public static String selectCountExists(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CASE WHEN ");
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(((EntityColumn) pkColumns.iterator().next()).getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }

        sql.append(" > 0 THEN 1 ELSE 0 END AS result ");
        return sql.toString();
    }

    public static String fromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ");
        sql.append(defaultTableName);
        sql.append(" ");
        return sql.toString();
    }

    public static String deleteFromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(defaultTableName);
        sql.append(" ");
        return sql.toString();
    }

    public static String notAllNullParameterCheck(String parameterName, Set<EntityColumn> columnSet) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"notAllNullParameterCheck\" value=\"@org.sweeter.mapper.core.util.OGNL@notAllNullParameterCheck(");
        sql.append(parameterName).append(", '");
        StringBuilder fields = new StringBuilder();

        EntityColumn column;
        for (Iterator var4 = columnSet.iterator(); var4.hasNext(); fields.append(column.getProperty())) {
            column = (EntityColumn) var4.next();
            if (fields.length() > 0) {
                fields.append(",");
            }
        }

        sql.append(fields);
        sql.append("')\"/>");
        return sql.toString();
    }

    public static String wherePKColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
        Iterator var3 = columnSet.iterator();

        while (var3.hasNext()) {
            EntityColumn column = (EntityColumn) var3.next();
            sql.append(" AND " + column.getColumnEqualsHolder((String) null));
        }

        sql.append("</where>");
        return sql.toString();
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        Iterator var4 = columnSet.iterator();

        while (var4.hasNext()) {
            EntityColumn column = (EntityColumn) var4.next();
            sql.append(getIfNotNull(column, " AND " + column.getColumnEqualsHolder(), empty));
        }

        sql.append("</where>");
        return sql.toString();
    }

    public static String orderByDefault(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append(" ORDER BY ");
            sql.append(orderByClause);
        }

        return sql.toString();
    }

    public static String exampleForUpdate() {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"@com.example.demo.configs.mapper.helper.OGNL@hasForUpdate(_parameter)\">");
        sql.append("FOR UPDATE");
        sql.append("</if>");
        return sql.toString();
    }

    public static String exampleCheck(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"checkExampleEntityClass\" value=\"@com.example.demo.configs.mapper.helper.OGNL@checkExampleEntityClass(_parameter, '");
        sql.append(entityClass.getCanonicalName());
        sql.append("')\"/>");
        return sql.toString();
    }

    public static String exampleSelectColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@com.example.demo.configs.mapper.helper.OGNL@hasSelectColumns(_parameter)\">");
        sql.append("<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">");
        sql.append("${selectColumn}");
        sql.append("</foreach>");
        sql.append("</when>");
        sql.append("<otherwise>");
        sql.append(getAllColumns(entityClass));
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }

    public static String exampleCountColumn(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@com.example.demo.configs.mapper.helper.OGNL@hasCountColumn(_parameter)\">");
        sql.append("COUNT(<if test=\"distinct\">distinct </if>${countColumn})");
        sql.append("</when>");
        sql.append("<otherwise>");
        sql.append("COUNT(*)");
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }

    public static String exampleWhereClause() {
        return "<if test=\"_parameter != null\"><where>\n  <foreach collection=\"oredCriteria\" item=\"criteria\">\n    <if test=\"criteria.valid\">\n      ${@com.example.demo.configs.mapper.helper.OGNL@andOr(criteria)}      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n          <choose>\n            <when test=\"criterion.noValue\">\n              ${@com.example.demo.configs.mapper.helper.OGNL@andOr(criterion)} ${criterion.condition}\n            </when>\n            <when test=\"criterion.singleValue\">\n              ${@com.example.demo.configs.mapper.helper.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value}\n            </when>\n            <when test=\"criterion.betweenValue\">\n              ${@com.example.demo.configs.mapper.helper.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n            </when>\n            <when test=\"criterion.listValue\">\n              ${@com.example.demo.configs.mapper.helper.OGNL@andOr(criterion)} ${criterion.condition}\n              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n                #{listItem}\n              </foreach>\n            </when>\n          </choose>\n        </foreach>\n      </trim>\n    </if>\n  </foreach>\n</where></if>";
    }

    public static String exampleOrderBy(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"orderByClause != null\">");
        sql.append("order by ${orderByClause}");
        sql.append("</if>");
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append("<if test=\"orderByClause == null\">");
            sql.append("ORDER BY " + orderByClause);
            sql.append("</if>");
        }

        return sql.toString();
    }
}

