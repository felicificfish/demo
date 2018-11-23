package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 礼品DO
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(of = "id")
public class GiftDO implements Serializable {
    /**
     * 奖品Id
     */
    private Integer id;
    /**
     * 奖品名称
     */
    private String name;
    /**
     * 获奖概率
     */
    private Double probability;
}
