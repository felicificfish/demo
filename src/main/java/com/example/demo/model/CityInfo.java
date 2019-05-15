package com.example.demo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 省市数据，树形结构
 *
 * @author zhou.xy
 * @since 2019/5/15
 */
@Data
public class CityInfo implements Serializable {
    private String cityCode;
    private String cityName;
    private List<CityInfo> children;
}
