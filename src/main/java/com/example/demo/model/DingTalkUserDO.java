package com.example.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 钉钉用户信息实体
 *
 * @author zhou.xy
 * @since 2019/6/14
 */
@Data
public class DingTalkUserDO implements Serializable {
    /**
     * 用户ID
     */
    private String userid;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户手机号
     */
    private String mobile;
}
