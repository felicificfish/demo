package com.example.demo.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author zhou.xy
 * @date 2019/8/27
 * @since 1.0
 */
@Data
public class UserLoginInfoVO implements Serializable {
    private static final long serialVersionUID = -8679497335806032622L;
    /**
     * 用户Token
     */
    private String userToken;
    /**
     * 用户信息
     */
    private UserInfoVO userInfo;
}
