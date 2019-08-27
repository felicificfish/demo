package com.example.demo.wechat.model;

import lombok.Data;

/**
 * 微信文本消息实体
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatTextMsg extends WeChatBaseMsg {
    /**
     * 消息内容
     */
    private String Content;
}
