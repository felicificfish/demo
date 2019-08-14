package com.example.demo.constant;

import lombok.Getter;

/**
 * 抽奖频率枚举
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
@Getter
public enum DrawFrequencyEnum {
    EVERYDAY(1, "每天"),
    EVERY_WEEK(2, "每周"),
    EVERY_MONTH(3, "每月"),
    ONLY_ONE(4, "仅一次");

    /**
     * 频率编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    DrawFrequencyEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据频率编码获取枚举
     *
     * @param code 频率编码
     * @return DrawFrequencyEnum
     */
    public static DrawFrequencyEnum getByCode(Integer code) {
        for (DrawFrequencyEnum frequencyEnum : DrawFrequencyEnum.values()) {
            if (frequencyEnum.getCode().equals(code)) {
                return frequencyEnum;
            }
        }
        return null;
    }
}
