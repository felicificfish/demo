package com.example.demo.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 微信公众号菜单信息
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
@Data
@EqualsAndHashCode(of = "menuId")
public class WechatOfficialAccountMenuVO implements Serializable {
    private static final long serialVersionUID = -8690642650535567171L;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 上级菜单ID
     */
    private Long menuPid;
    /**
     * 菜单名称
     */
    private String menuName;
}