package com.example.demo.configs.mapper.utils;

import com.example.demo.configs.exception.ValidateException;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.MappedStatement;

public class MsUtils {
    public static final Cache CLASS_CACHE = new SoftCache(new PerpetualCache("MAPPER_CLASS_CACHE"));

    public MsUtils() {
    }

    public static Class<?> getMapperClass(String msId) {
        if (msId.indexOf(".") == -1) {
            throw new ValidateException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        } else {
            String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
            Class<?> mapperClass = (Class) CLASS_CACHE.getObject(mapperClassStr);
            if (mapperClass != null) {
                return mapperClass;
            } else {
                ClassLoader[] classLoader = getClassLoaders();
                ClassLoader[] var4 = classLoader;
                int var5 = classLoader.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    ClassLoader cl = var4[var6];
                    if (null != cl) {
                        try {
                            mapperClass = Class.forName(mapperClassStr, true, cl);
                            if (mapperClass != null) {
                                break;
                            }
                        } catch (ClassNotFoundException var9) {
                            ;
                        }
                    }
                }

                if (mapperClass == null) {
                    throw new ValidateException("class loaders failed to locate the class " + mapperClassStr);
                } else {
                    CLASS_CACHE.putObject(mapperClassStr, mapperClass);
                    return mapperClass;
                }
            }
        }
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{Thread.currentThread().getContextClassLoader(), MsUtils.class.getClassLoader()};
    }

    public static String getMethodName(MappedStatement ms) {
        return getMethodName(ms.getId());
    }

    public static String getMethodName(String msId) {
        return msId.substring(msId.lastIndexOf(".") + 1);
    }
}
