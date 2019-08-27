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
public class UserSettingInfoVO implements Serializable {
    private static final long serialVersionUID = 3039494370322722692L;

    private String userName;
    private String mobile;
    private String wechatNo;
    private String slogan;
    private String photo;
    private String photoH5;
    private String qrCode;
}
