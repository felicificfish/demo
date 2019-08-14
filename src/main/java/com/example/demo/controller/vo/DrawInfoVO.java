package com.example.demo.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户抽奖次数实体
 *
 * @author zhou.xy
 * @date 2019/8/5
 * @since 1.0
 */
@Data
public class DrawInfoVO implements Serializable {
    private static final long serialVersionUID = -7229096591062847226L;
    /**
     * 可抽奖次数
     */
    private Integer drawCount;
    /**
     * 抽奖类型详细信息
     */
    private List<DrawItemVO> drawItemList;
}
