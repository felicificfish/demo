package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Config;
import com.example.demo.configs.mapper.entity.EntityColumn;
import com.example.demo.configs.mapper.entity.EntityTable;
import com.example.demo.configs.mapper.utils.MetaObjectUtils;
import com.example.demo.configs.mapper.utils.MsUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MapperTemplate {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    protected Map<String, Method> methodMap = new ConcurrentHashMap();
    protected Map<String, Class<?>> entityClassMap = new ConcurrentHashMap();
    protected Class<?> mapperClass;
    protected MapperHelper mapperHelper;

    public MapperTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
        this.mapperClass = mapperClass;
        this.mapperHelper = mapperHelper;
    }

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

    public void addMethodMap(String methodName, Method method) {
        this.methodMap.put(methodName, method);
    }

    public String getIDENTITY(EntityColumn column) {
        return MessageFormat.format(this.mapperHelper.getConfig().getIDENTITY(), column.getColumn(), column.getProperty(), column.getTable().getName());
    }

    public boolean supportMethod(String msId) {
        Class<?> mapperClass = MsUtils.getMapperClass(msId);
        if (mapperClass != null && this.mapperClass.isAssignableFrom(mapperClass)) {
            String methodName = MsUtils.getMethodName(msId);
            return this.methodMap.get(methodName) != null;
        } else {
            return false;
        }
    }

    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        List<ResultMap> resultMaps = new ArrayList();
        resultMaps.add(entityTable.getResultMap(ms.getConfiguration()));
        MetaObject metaObject = MetaObjectUtils.forObject(ms);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
    }

    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = MetaObjectUtils.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    public SqlSource createSqlSource(MappedStatement ms, String xmlSql) {
        return languageDriver.createSqlSource(ms.getConfiguration(), "<script>\n\t" + xmlSql + "</script>", (Class) null);
    }

    public Class<?> getEntityClass(MappedStatement ms) {
        String msId = ms.getId();
        if (this.entityClassMap.containsKey(msId)) {
            return (Class) this.entityClassMap.get(msId);
        } else {
            Class<?> mapperClass = MsUtils.getMapperClass(msId);
            Type[] types = mapperClass.getGenericInterfaces();
            Type[] var5 = types;
            int var6 = types.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Type type = var5[var7];
                if (type instanceof ParameterizedType) {
                    ParameterizedType t = (ParameterizedType) type;
                    if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class) t.getRawType())) {
                        Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                        EntityHelper.initEntityNameMap(returnType, this.mapperHelper.getConfig());
                        this.entityClassMap.put(msId, returnType);
                        return returnType;
                    }
                }
            }

            throw new ValidateException("无法获取 " + msId + " 方法的泛型信息!");
        }
    }

    protected String tableName(Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (StringUtils.isEmpty(prefix)) {
            prefix = this.mapperHelper.getConfig().getPrefix();
        }

        return !StringUtils.isEmpty(prefix) ? prefix + "." + entityTable.getName() : entityTable.getName();
    }

    public Config getConfig() {
        return this.mapperHelper.getConfig();
    }

    public String getIDENTITY() {
        return this.getConfig().getIDENTITY();
    }

    public boolean isBEFORE() {
        return this.getConfig().isBEFORE();
    }

    public boolean isCheckExampleEntityClass() {
        return this.getConfig().isCheckExampleEntityClass();
    }

    public boolean isNotEmpty() {
        return this.getConfig().isNotEmpty();
    }

    public void setSqlSource(MappedStatement ms) throws Exception {
        if (this.mapperClass == MsUtils.getMapperClass(ms.getId())) {
            throw new ValidateException("请不要配置或扫描通用Mapper接口类：" + this.mapperClass);
        } else {
            Method method = (Method) this.methodMap.get(MsUtils.getMethodName(ms));

            try {
                if (method.getReturnType() == Void.TYPE) {
                    method.invoke(this, ms);
                } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                    SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                    DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                    this.setSqlSource(ms, dynamicSqlSource);
                } else {
                    if (!String.class.equals(method.getReturnType())) {
                        throw new ValidateException("自定义Mapper方法返回类型错误,可选的返回类型为void,SqlNode,String三种!");
                    }

                    String xmlSql = (String) method.invoke(this, ms);
                    SqlSource sqlSource = this.createSqlSource(ms, xmlSql);
                    this.setSqlSource(ms, sqlSource);
                }

            } catch (IllegalAccessException var5) {
                throw new ValidateException(var5.getMessage());
            } catch (InvocationTargetException var6) {
                throw new ValidateException(var6.getTargetException() != null ? var6.getTargetException().getMessage() : var6.getMessage());
            }
        }
    }
}
