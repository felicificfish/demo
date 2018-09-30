package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * 省市数据
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "fast_city_code")
public class CityCodeDO implements Serializable {
    private Long id;

    private String cityCode;

    private String cityName;

    private String parentCode;
}