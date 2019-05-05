package com.example.demo.utils;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.ReflectionUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 对象比较工具
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Log4j2
public class BeanCompareUtil {
    private static final Map<String, Set<Field>> FIELD_CACHE = new HashMap<>();
    private static final Map<String, Set<Method>> METHOD_CACHE = new HashMap<>();
    private static final Set<String> IGNORE_FIELDS = new HashSet<>();
    private static Pattern UPPER_STR_PATTERN = Pattern.compile("[A-Z]");

    static {
        // TODO 添加忽略字段
        IGNORE_FIELDS.add("serialVersionUID");
        IGNORE_FIELDS.add("creatorId");
        IGNORE_FIELDS.add("creator");
        IGNORE_FIELDS.add("createdon");
        IGNORE_FIELDS.add("modifierId");
        IGNORE_FIELDS.add("modifier");
        IGNORE_FIELDS.add("modifiedon");
    }

    /**
     * 比较两个对象，返回属性及变更信息<br>
     * 前提是两个对象都不是null，且两个对象为同一类型
     *
     * @param oldObj 原纪录
     * @param newObj 新纪录
     * @return List<CompareResult> 比较结果
     */
    public static List<CompareResult> compare(Object oldObj, Object newObj) {
        List<CompareResult> compareResults = new ArrayList<>();

        if (null == oldObj || null == newObj || oldObj == newObj || !oldObj.getClass().equals(newObj.getClass())) {
            return compareResults;
        }

        Set<Field> fields = getField(oldObj.getClass());
        if (CollectionUtils.isEmpty(fields)) {
            return compareResults;
        }
        Set<Method> methods = getMethod(oldObj.getClass());

        String methodName;
        String fieldName;
        for (Field field : fields) {
            // 有Transient注解的不是本对象的数据库字段，忽略
            if (null != field.getAnnotation(Transient.class)) {
                continue;
            }
            fieldName = field.getName();
            if (IGNORE_FIELDS.contains(fieldName)) {
                continue;
            }
            methodName = "get".concat(fieldName.substring(0, 1).toUpperCase()).concat(fieldName.substring(1));
            Method method = filterMethod(methods, methodName);
            // boolean类型需要特殊处理
            if (null == method) {
                if ("boolean".equals(field.getType().getTypeName())) {
                    if (fieldName.startsWith("is")) {
                        boolean isUp = UPPER_STR_PATTERN.matcher(fieldName.substring(2, 3)).matches();
                        if (isUp) {
                            methodName = fieldName;
                        } else {
                            methodName = "is".concat(fieldName.substring(0, 1).toUpperCase()).concat(fieldName.substring(1));
                        }
                    } else {
                        methodName = "is".concat(fieldName.substring(0, 1).toUpperCase()).concat(fieldName.substring(1));
                    }
                    method = filterMethod(methods, methodName);
                }
                if (null == method) {
                    continue;
                }
            }

            try {
                Object oldValue = method.invoke(oldObj);
                Object newValue = method.invoke(newObj);
                if (null == oldValue) {
                    if (null != newValue) {
                        compareResults.add(CompareResult.builder().column(fieldName).difference(
                                Difference.builder().oldValue(oldValue).newValue(newValue).build()
                        ).build());
                    }
                } else {
                    if (null != newValue) {
                        if (newValue instanceof BigDecimal) {
                            BigDecimal newValueBigDecimal = (BigDecimal) newValue;
                            BigDecimal oldValueBigDecimal = (BigDecimal) oldValue;
                            if (newValueBigDecimal.compareTo(oldValueBigDecimal) != 0) {
                                compareResults.add(CompareResult.builder().column(fieldName).difference(
                                        Difference.builder().oldValue(oldValue).newValue(newValue).build()
                                ).build());
                            }
                        } else if (!oldValue.equals(newValue)) {
                            compareResults.add(CompareResult.builder().column(fieldName).difference(
                                    Difference.builder().oldValue(oldValue).newValue(newValue).build()
                            ).build());
                        }
                    } else {
                        compareResults.add(CompareResult.builder().column(fieldName).difference(
                                Difference.builder().oldValue(oldValue).newValue(newValue).build()
                        ).build());
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("反射调用get方法异常", e);
            }
        }

        return compareResults;
    }

    /**
     * 获取对象的所有属性
     *
     * @param cls 对象
     * @return Set<Field> 属性集合
     */
    public static Set<Field> getField(Class<?> cls) {
        Set<Field> fields = FIELD_CACHE.get(cls.getName());
        if (CollectionUtils.isEmpty(fields)) {
            synchronized (cls.getName()) {
                if (CollectionUtils.isEmpty(fields)) {
                    fields = ReflectionUtils.getAllFields(cls);
                    FIELD_CACHE.put(cls.getName(), fields);
                }
            }
        }
        return fields;
    }

    /**
     * 获取对象的所有方法
     *
     * @param cls 对象
     * @return Set<Method> 方法集合
     */
    public static Set<Method> getMethod(Class<?> cls) {
        Set<Method> methods = METHOD_CACHE.get(cls.getName());
        if (CollectionUtils.isEmpty(methods)) {
            synchronized (cls.getName()) {
                if (CollectionUtils.isEmpty(methods)) {
                    methods = ReflectionUtils.getAllMethods(cls);
                    METHOD_CACHE.put(cls.getName(), methods);
                }
            }
        }
        return methods;
    }

    /**
     * 过滤方法
     *
     * @param methods    方法集合
     * @param methodName 方法名
     * @return Method 方法
     */
    public static Method filterMethod(Set<Method> methods, String methodName) {
        if (!CollectionUtils.isEmpty(methods)) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }
        return null;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompareResult implements Serializable {
        private String column;
        private Difference difference;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Difference implements Serializable {
        private Object oldValue;
        private Object newValue;
    }
}
