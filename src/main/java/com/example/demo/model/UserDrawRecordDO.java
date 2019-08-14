package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户抽奖记录
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
@Data
@Table(name = "trans_user_draw_record")
@EqualsAndHashCode(of = "drawId")
@Alias("userDrawRecord")
public class UserDrawRecordDO implements Serializable {
    /**
     * 0-未抽奖
     */
    public static final Integer DRAW_NO = 0;
    /**
     * 1-已抽奖
     */
    public static final Integer DRAW_YES = 1;
    private static final long serialVersionUID = -2482971277458359644L;
    /**
     * 抽奖ID
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawId;
    /**
     * 活动类型
     */
    private Integer activityType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 抽奖优先级，值越小，优先级越高
     */
    private Integer level;
    /**
     * 抽奖类型编码：1-20积分兑换；2-订单金额≥1万；
     */
    private Integer drawCode;
    /**
     * 抽奖描述
     */
    private String drawDesc;
    /**
     * 抽奖状态：0-未抽奖；1-已抽奖；
     */
    private Integer ifDraw;
    /**
     * 奖品编号
     */
    private Integer prizeCode;
    /**
     * 抽奖时间
     */
    private Date drawTime;
    /**
     * 创建时间
     */
    private Date createdon;
}