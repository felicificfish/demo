package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 奖品信息
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
@Data
@Table(name = "trans_prize_config")
@EqualsAndHashCode(of = "prizeId")
@Alias("prizeConfig")
public class PrizeConfigDO implements Serializable {
    /**
     * 奖品编号
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prizeId;
    /**
     * 奖品编号
     */
    private String prizeCode;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 中奖概率
     */
    private Double prizeProbability;
    /**
     * 活动类型，对应活动表中的活动类型
     */
    private Integer activityType;
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