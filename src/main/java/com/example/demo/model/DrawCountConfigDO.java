package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 抽奖次数获取配置
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
@Data
@Table(name = "trans_draw_count_config")
@EqualsAndHashCode(of = "configId")
@Alias("drawCountConfig")
public class DrawCountConfigDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 抽奖配置ID
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;
    /**
     * 活动类型
     */
    private Integer activityType;
    /**
     * 抽奖编号
     */
    private Integer drawCode;
    /**
     * 抽奖描述
     */
    private String drawDesc;
    /**
     * 阈值
     */
    private BigDecimal drawValue;
    /**
     * 抽奖优先级，值越小，优先级越高
     */
    private Integer level;
    /**
     * 抽奖频率：1-每天；2-每周；3-每月；4-仅一次；
     */
    private Integer frequency;
    /**
     * 创建时间
     */
    private Date createdon;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建人ID
     */
    private Long creatorId;
    /**
     * 修改时间
     */
    private Date modifiedon;
    /**
     * 修改人
     */
    private String modifier;
    /**
     * 修改人ID
     */
    private Long modifierId;
}