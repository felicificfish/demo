package com.example.demo.wechat.model;

import lombok.Data;

/**
 * 微信消息公共属性
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatBaseMsg {

    private String URL;

    private long MsgId;
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String ToUserName;
    /**
     * 开发者微信号
     */
    private String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private long CreateTime;
    /**
     * 消息类型（text/music/news）
     */
    private String MsgType;
}
