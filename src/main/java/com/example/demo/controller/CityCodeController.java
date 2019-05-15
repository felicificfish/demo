package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.CityCodeDO;
import com.example.demo.model.CityInfo;
import com.example.demo.service.CityCodeService;
import com.example.demo.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 省市数据控制器
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
@RestController
public class CityCodeController {
    @Autowired
    private CityCodeService cityCodeService;

    @GetMapping(value = "/initCityData")
    public void initCityData() {
        List<CityCodeDO> cityCodeDoList = cityCodeService.queryCityData();
        String content = "var MSCitysv = ";

        List<JSONObject> cityDatas = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cityCodeDoList)) {
            for (CityCodeDO cityCode : cityCodeDoList) {
                String parentCode = cityCode.getParentCode();
                if ("1".equals(parentCode)) {
                    JSONObject city = new JSONObject();
                    city.put("name", cityCode.getCityName());
                    List<JSONObject> subCityDatas = new ArrayList<>();
                    for (CityCodeDO subCityCode : cityCodeDoList) {
                        if (Objects.equals(cityCode.getCityCode(), subCityCode.getParentCode())) {
                            JSONObject subCity = new JSONObject();
                            subCity.put("name", subCityCode.getCityName());
                            List<String> areas = new ArrayList<>();
                            for (CityCodeDO areaCode : cityCodeDoList) {
                                if (Objects.equals(areaCode.getParentCode(), subCityCode.getCityCode())) {
                                    areas.add(areaCode.getCityName());
                                }
                            }
                            subCity.put("area", areas);
                            subCityDatas.add(subCity);
                        }
                    }
                    city.put("city", subCityDatas);
                    cityDatas.add(city);
                }
            }
        }
        content = content + JSON.toJSONString(cityDatas);

        FileUtil.writeFile(content, "D:\\WORKS", "e.js");
    }

    @GetMapping(value = "/getCityData")
    public void dataProcessing() {
        List<CityCodeDO> cityCodeDoList = cityCodeService.queryCityData();
        if (!CollectionUtils.isEmpty(cityCodeDoList)) {
            CityInfo root = new CityInfo();
            root.setCityCode("1");
            recursiveProcessing(root, cityCodeDoList);

            log.info(JSON.toJSONString(root));
        }
    }

    /**
     * 递归处理
     *
     * @param parent
     * @param cityCodeList
     */
    public void recursiveProcessing(CityInfo parent, List<CityCodeDO> cityCodeList) {
        if (CollectionUtils.isEmpty(cityCodeList)) {
            return;
        }
        cityCodeList.forEach(cityCodeDO -> {
            if (parent.getCityCode().equals(cityCodeDO.getParentCode())) {
                List<CityInfo> children = parent.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    children = new ArrayList<>();
                }
                CityInfo cityInfo = new CityInfo();
                cityInfo.setCityCode(cityCodeDO.getCityCode());
                cityInfo.setCityName(cityCodeDO.getCityName());
                children.add(cityInfo);
                parent.setChildren(children);

                recursiveProcessing(cityInfo, cityCodeList);
            }
        });
    }

}
