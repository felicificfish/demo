package com.example.demo.controller.vo;

import java.io.Serializable;

/**
 * 用户抽奖次数详细实体
 *
 * @author zhou.xy
 * @date 2019/8/5
 * @since 1.0
 */
public class DrawItemVO implements Serializable {
    /**
     * 抽奖编码
     */
    private Integer drawCode;
    /**
     * 生效状态
     */
    private Boolean available;
    /**
     * 描述
     */
    private String desc;

    public Integer getDrawCode() {
        return drawCode;
    }

    public void setDrawCode(Integer drawCode) {
        this.drawCode = drawCode;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
