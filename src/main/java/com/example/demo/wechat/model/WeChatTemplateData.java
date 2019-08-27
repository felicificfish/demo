package com.example.demo.wechat.model;

import lombok.Data;

/**
 * 微信模板数据
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatTemplateData {
    /**
     * 模板内容
     */
    private String value;
    /**
     * 模板内容字体颜色，不填默认为黑色
     */
    private String color;
}
