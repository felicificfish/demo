package com.example.demo.configs.mapper.utils;

import com.example.demo.configs.exception.ValidateException;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Method;

public class MetaObjectUtils {
    public static Method method;

    public MetaObjectUtils() {
    }

    public static MetaObject forObject(Object object) {
        try {
            return (MetaObject) method.invoke((Object) null, object);
        } catch (Exception var2) {
            throw new ValidateException(var2.getMessage());
        }
    }

    static {
        try {
            Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.SystemMetaObject");
            method = metaClass.getDeclaredMethod("forObject", Object.class);
        } catch (Exception var3) {
            try {
                Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.MetaObject");
                method = metaClass.getDeclaredMethod("forObject", Object.class);
            } catch (Exception var2) {
                throw new ValidateException(var2.getMessage());
            }
        }

    }
}

