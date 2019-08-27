package com.example.demo.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author zhou.xy
 * @date 2019/8/27
 * @since 1.0
 */
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 2294302950909519026L;

    /**
     * 用户姓名
     */
    private String userName;
}
