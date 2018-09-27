package com.example.demo.configs.mapper.helper;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.EntityColumn;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.configs.mapper.entity.IDynamicTableName;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public abstract class OGNL {
    public static final String SAFE_DELETE_ERROR = "通用 Mapper 安全检查: 对查询条件参数进行检查时出错!";
    public static final String SAFE_DELETE_EXCEPTION = "通用 Mapper 安全检查: 当前操作的方法没有指定查询条件，不允许执行该操作!";

    public OGNL() {
    }

    public static boolean checkExampleEntityClass(Object parameter, String entityFullName) {
        if (parameter != null && parameter instanceof Example && !StringUtils.isEmpty(entityFullName)) {
            Example example = (Example) parameter;
            Class<?> entityClass = example.getEntityClass();
            if (!entityClass.getCanonicalName().equals(entityFullName)) {
                throw new ValidateException("当前 Example 方法对应实体为:" + entityFullName + ", 但是参数 Example 中的 entityClass 为:" + entityClass.getCanonicalName());
            }
        }

        return true;
    }

    public static boolean notAllNullParameterCheck(Object parameter, String fields) {
        if (parameter != null) {
            try {
                Set<EntityColumn> columns = EntityHelper.getColumns(parameter.getClass());
                Set<String> fieldSet = new HashSet(Arrays.asList(fields.split(",")));
                Iterator var4 = columns.iterator();

                while (var4.hasNext()) {
                    EntityColumn column = (EntityColumn) var4.next();
                    if (fieldSet.contains(column.getProperty())) {
                        Object value = column.getEntityField().getValue(parameter);
                        if (value != null) {
                            return true;
                        }
                    }
                }
            } catch (Exception var7) {
                throw new ValidateException("通用 Mapper 安全检查: 对查询条件参数进行检查时出错!");
            }
        }

        throw new ValidateException("通用 Mapper 安全检查: 当前操作的方法没有指定查询条件，不允许执行该操作!");
    }

    public static boolean exampleHasAtLeastOneCriteriaCheck(Object parameter) {
        if (parameter != null) {
            try {
                if (parameter instanceof Example) {
                    List<Example.Criteria> criteriaList = ((Example) parameter).getOredCriteria();
                    if (criteriaList != null && criteriaList.size() > 0) {
                        return true;
                    }
                } else {
                    Method getter = parameter.getClass().getDeclaredMethod("getOredCriteria");
                    Object list = getter.invoke(parameter);
                    if (list != null && list instanceof List && ((List) list).size() > 0) {
                        return true;
                    }
                }
            } catch (Exception var3) {
                throw new ValidateException("通用 Mapper 安全检查: 对查询条件参数进行检查时出错!");
            }
        }

        throw new ValidateException("通用 Mapper 安全检查: 当前操作的方法没有指定查询条件，不允许执行该操作!");
    }

    public static boolean hasSelectColumns(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            if (example.getSelectColumns() != null && example.getSelectColumns().size() > 0) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasCountColumn(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            return !StringUtils.isEmpty(example.getCountColumn());
        } else {
            return false;
        }
    }

    public static boolean hasForUpdate(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            return example.isForUpdate();
        } else {
            return false;
        }
    }

    public static boolean hasNoSelectColumns(Object parameter) {
        return !hasSelectColumns(parameter);
    }

    public static boolean isDynamicParameter(Object parameter) {
        return parameter != null && parameter instanceof IDynamicTableName;
    }

    public static boolean isNotDynamicParameter(Object parameter) {
        return !isDynamicParameter(parameter);
    }

    public static String andOr(Object parameter) {
        if (parameter instanceof Example.Criteria) {
            return ((Example.Criteria) parameter).getAndOr();
        } else if (parameter instanceof Example.Criterion) {
            return ((Example.Criterion) parameter).getAndOr();
        } else {
            return parameter.getClass().getCanonicalName().endsWith("Criteria") ? "or" : "and";
        }
    }
}
