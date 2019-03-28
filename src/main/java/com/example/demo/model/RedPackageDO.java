package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 红包
 *
 * @author zhou.xy
 * @since 2019/3/28
 */
@Data
@AllArgsConstructor
public class RedPackageDO implements Serializable {
    private Integer remainSize;
    private Double remainMoney;
}
