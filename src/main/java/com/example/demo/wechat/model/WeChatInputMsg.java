package com.example.demo.wechat.model;

import lombok.Data;

/**
 * 微信消息实体
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatInputMsg extends WeChatBaseMsg {
    /**
     * 文本消息
     */
    private String Content;
    /**
     * 图片消息
     */
    private String PicUrl;
    /**
     * 位置消息
     */
    private String Location_X;
    private String Location_Y;
    private Long Scale;
    private String Label;
    /**
     * 链接消息
     */
    private String Title;
    private String Description;
    private String Url;
    /**
     * 语音信息
     */
    private String MediaId;
    private String Format;
    private String Recognition;
    /**
     * 视频信息
     */
    private String ThumbMediaId;
    /**
     * 事件
     * 事件类型
     */
    private String Event;
    /**
     * 事件KEY值
     */
    private String EventKey;
    /**
     * 二维码的ticket
     */
    private String Ticket;
    /**
     * 上报地理位置事件
     * 地理位置纬度
     */
    private String Latitude;
    /**
     * 地理位置经度
     */
    private String Longitude;
    /**
     * 地理位置精度
     */
    private String Precision;
}