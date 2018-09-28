package com.example.demo.enums;

/**
 * RegexType
 *
 * @author zhou.xy
 * @date 2018/09/28
 */
public enum RegexType {
    /**
     * 校验email正则
     */
    EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
    /**
     * 校验手机号正则
     */
    MOBILE_PHONE("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$"),
    /**
     * 校验电话正则
     */
    TELEPHONE("^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$"),
    /**
     * 校验中文正则
     */
    CHINESE("^[\u4E00-\u9FA5]+$"),
    /**
     * 校验IP正则
     */
    IP("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    private String value;

    RegexType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
