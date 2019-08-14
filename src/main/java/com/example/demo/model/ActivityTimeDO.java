package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 活动时间表
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
@Data
@Table(name = "trans_activity_time")
@EqualsAndHashCode(of = "activityTimeId")
@Alias("activityTime")
public class ActivityTimeDO implements Serializable {
    /**
     * id
     */
    @Id
    @OrderBy("DESC")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityTimeId;
    /**
     * 活动类型
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
}