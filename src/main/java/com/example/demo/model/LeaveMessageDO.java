package com.example.demo.model;

import lombok.Data;

import java.util.Date;

/**
 * 留言信息
 *
 * @author zhou.xy
 * @date 2019/9/11
 * @since 1.0
 */
@Data
public class LeaveMessageDO {
    /**
     * 发送
     */
    public static final Integer OPT_SEND = 1;
    /**
     * 撤回
     */
    public static final Integer OPT_WITHDRAW = 2;

    /**
     * 留言ID
     */
    private Long messageId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     *
     */
    private String avatar;
    /**
     * 留言
     */
    private String message;
    /**
     * 留言时间
     */
    private Date sendTime;
    /**
     * 1-发送；2-撤回
     */
    private Integer opt;
}
