package com.example.demo.model;

import lombok.Data;

/**
 * 地址信息
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
public class AddressDO {
    private String ip;
    /** 国 */
    private String country;
    private String countryId;
    /** 省 */
    private String region;
    private String regionId;
    /** 市 */
    private String city;
    private String cityId;
    /** 区 */
    private String area;
    private String areaId;
    private String county;
    private String countyId;
    /** 运营商 */
    private String isp;
    private String ispId;
}
