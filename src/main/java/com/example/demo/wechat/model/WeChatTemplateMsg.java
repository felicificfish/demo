package com.example.demo.wechat.model;

import lombok.Data;

import java.util.Map;

/**
 * 微信模板消息实体
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatTemplateMsg {
    /**
     * 接收者openid
     */
    private String touser;
    /**
     * 模板ID
     */
    private String template_id;
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private WeChatMiniProgram miniprogram;
    /**
     * 模板数据
     */
    private Map<String, WeChatTemplateData> data;
}
