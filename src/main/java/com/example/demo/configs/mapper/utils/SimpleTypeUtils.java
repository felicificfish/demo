package com.example.demo.configs.mapper.utils;

import com.example.demo.configs.exception.ValidateException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SimpleTypeUtils {
    public static final String[] JAVA8_DATE_TIME = new String[]{"java.time.Instant", "java.time.LocalDateTime", "java.time.LocalDate", "java.time.LocalTime", "java.time.OffsetDateTime", "java.time.OffsetTime", "java.time.ZonedDateTime", "java.time.Year", "java.time.Month", "java.time.YearMonth"};
    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet();

    public SimpleTypeUtils() {
    }

    public static void registerSimpleType(Class<?> clazz) {
        SIMPLE_TYPE_SET.add(clazz);
    }

    public static void registerPrimitiveTypes() {
        registerSimpleType(Boolean.TYPE);
        registerSimpleType(Byte.TYPE);
        registerSimpleType(Short.TYPE);
        registerSimpleType(Integer.TYPE);
        registerSimpleType(Long.TYPE);
        registerSimpleType(Character.TYPE);
        registerSimpleType(Float.TYPE);
        registerSimpleType(Double.TYPE);
    }

    public static void registerSimpleType(String classes) {
        if (!StringUtils.isEmpty(classes)) {
            String[] cls = classes.split(",");
            String[] var2 = cls;
            int var3 = cls.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String c = var2[var4];

                try {
                    SIMPLE_TYPE_SET.add(Class.forName(c));
                } catch (ClassNotFoundException var7) {
                    throw new ValidateException("注册类型出错:" + c);
                }
            }
        }

    }

    private static void registerSimpleTypeSilence(String clazz) {
        try {
            SIMPLE_TYPE_SET.add(Class.forName(clazz));
        } catch (ClassNotFoundException var2) {
            ;
        }

    }

    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPE_SET.contains(clazz);
    }

    static {
        SIMPLE_TYPE_SET.add(byte[].class);
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Timestamp.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);
        String[] var0 = JAVA8_DATE_TIME;
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            String time = var0[var2];
            registerSimpleTypeSilence(time);
        }

    }
}

