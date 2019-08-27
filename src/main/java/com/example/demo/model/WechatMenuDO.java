package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 微信公众号菜单信息实体
 *
 * @author zhou.xy
 * @since 2019/6/10
 */
@Data
@Table(name = "trans_wechat_menu")
@EqualsAndHashCode(of = "menuId")
@Alias("wechatMenu")
public class WechatMenuDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    /**
     * 公众号开发者ID
     */
    private String appId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单等级
     */
    private Integer menuLeve;
    /**
     * 上级菜单ID
     */
    private Long menuPid;
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
     * 地址
     */
    private String url;
    /**
     * 消息类型
     */
    private String eventType;
    /**
     * 消息Id
     */
    private Long msgId;
    /**
     * 关键字
     */
    private String inputCode;
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
}