package com.example.demo.utils;

import com.example.demo.enums.RegexType;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author zhou.xy
 * @date 2018/9/20 13:48
 * @since 1.0
 */
public class ValidateUtil {
    /**
     * 判断对象是否Empty(null或元素个数为0)
     *
     * @param object 要判断的对象
     * @return boolean
     */
    public static boolean isEmpty(Object object) {
        if (null == object || "".equals(object)) {
            return true;
        }
        if (object instanceof String && ((String) object).trim().length() == 0) {
            return true;
        }
        if (object instanceof Collection<?> && ((Collection<?>) object).isEmpty()) {
            return true;
        }
        return object instanceof Map && ((Map) object).size() == 0;
    }

    /**
     * 判断对象是否NotEmpty(!null或有元素)
     *
     * @param object 要判断的对象
     * @return boolean
     */
    public static boolean isNotEmpty(Object object) {
        if (null == object || "".equals(object)) {
            return false;
        }
        if (object instanceof String && ((String) object).trim().length() == 0) {
            return false;
        }
        if (object instanceof Collection<?> && ((Collection<?>) object).isEmpty()) {
            return false;
        }
        return !(object instanceof Map && ((Map) object).size() == 0);
    }

    /**
     * 校验IP
     *
     * @param ip IP地址
     * @return boolean
     */
    public static boolean isIP(String ip) {
        if (isEmpty(ip)) {
            return false;
        }
        return ip.matches(RegexType.IP.value());
    }

    /**
     * 校验Email
     *
     * @param email 邮箱地址
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return email.matches(RegexType.EMAIL.value());
    }

    /**
     * 校验手机号
     *
     * @param mobilePhone 手机号
     * @return boolean
     */
    public static boolean isMobilePhone(String mobilePhone) {
        if (isEmpty(mobilePhone)) {
            return false;
        }
        return mobilePhone.matches(RegexType.MOBILE_PHONE.value());
    }

    /**
     * 校验固话
     *
     * @param telephone 固话
     * @return boolean
     */
    public static boolean isTelephone(String telephone) {
        if (isEmpty(telephone)) {
            return false;
        }
        return telephone.matches(RegexType.TELEPHONE.value());
    }

    /**
     * 校验是否包含特殊字符
     *
     * @param text 字符串
     * @return boolean
     */
    public static boolean hasSpecialChars(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return text.replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0;
    }

    /**
     * 判断是否纯中文，仅适合中国汉字，不包括标点
     *
     * @param text 字符串
     * @return boolean
     */
    public static boolean isChinese(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return Pattern.compile(RegexType.CHINESE.value()).matcher(text).find();
    }

}
