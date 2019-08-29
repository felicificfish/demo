package com.example.demo.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信公众号菜单信息
 *
 * @author zhou.xy
 * @since 2019/8/29
 */
@Data
public class WechatOfficialAccountMenuDTO implements Serializable {
    private static final long serialVersionUID = -7996271817397573837L;

    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 上级菜单ID
     */
    @NotNull(message = "上级菜单不能为空")
    private Long menuPid;
    /**
     * 菜单等级：1-一级菜单；2-二级菜单；...
     */
    @NotNull(message = "菜单等级不能为空")
    private Integer menuLevel;
    /**
     * 公众号开发者ID
     */
    @NotBlank(message = "公众号开发者ID不能为空")
    @Length(max = 32, message = "公众号开发者ID的长度不能超过" + 32)
    private String appId;
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 60, message = "菜单名称的长度不能超过" + 60)
    private String menuName;
    /**
     * 排序
     */
    @NotNull(message = "排序值不能为空")
    private Integer sort;
    /**
     * 菜单类别
     */
    @NotBlank(message = "菜单类别不能为空")
    @Length(max = 16, message = "菜单类别的长度不能超过" + 32)
    private String menuType;
    /**
     * 小程序的页面路径
     */
    @Length(max = 128, message = "小程序的页面路径的长度不能超过" + 128)
    private String pagePath;
    /**
     * 跳转地址
     */
    @Length(max = 128, message = "跳转地址的长度不能超过" + 128)
    private String url;
    /**
     * 小程序的appId（仅认证公众号可配置）
     */
    @Length(max = 32, message = "小程序的appId的长度不能超过" + 32)
    private String miniProgramAppId;
    /**
     * 消息Id
     */
    private Long msgId;
    /**
     * 关键字
     */
    @Length(max = 64, message = "关键字的长度不能超过" + 64)
    private String keyword;
}