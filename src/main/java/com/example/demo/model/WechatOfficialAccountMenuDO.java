package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 微信公众号菜单信息
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
@Data
@Table(name = "trans_wechat_official_account_menu")
@EqualsAndHashCode(of = "menuId")
@Alias("wechatOfficialAccountMenu")
public class WechatOfficialAccountMenuDO implements Serializable {
    private static final long serialVersionUID = -8189097779277957289L;
    /**
     * 菜单ID
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    /**
     * 上级菜单ID
     */
    private Long menuPid;
    /**
     * 菜单等级：1-一级菜单；2-二级菜单；...
     */
    private Integer menuLevel;
    /**
     * 公众号开发者ID
     */
    private String appId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 是否启用：1-启用；0-禁用
     */
    private Integer isEnable;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 菜单类别
     */
    private String menuType;
    /**
     * 小程序的页面路径
     */
    private String pagePath;
    /**
     * 地址
     */
    private String url;
    /**
     * 小程序的appid（仅认证公众号可配置）
     */
    private String miniProgramAppId;
    /**
     * 消息Id
     */
    private Long msgId;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 创建人ID
     */
    private Long creatorId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createdon;
    /**
     * 修改人ID
     */
    private Long modifierId;
    /**
     * 修改人
     */
    private String modifier;
    /**
     * 修改时间
     */
    private Date modifiedon;
    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDel;

    /**
     * 子菜单
     */
    @Transient
    private List<WechatOfficialAccountMenuDO> children;
    /**
     * 菜单ID集合
     */
    @Transient
    private List<Long> menuIdList;
}