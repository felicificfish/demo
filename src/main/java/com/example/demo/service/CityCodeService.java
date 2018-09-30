package com.example.demo.service;

import com.example.demo.dao.CityCodeMapper;
import com.example.demo.model.CityCodeDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市数据Service
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
@Service
public class CityCodeService {
    @Autowired
    private CityCodeMapper cityCodeMapper;

    public List<CityCodeDO> queryCityData() {
        return cityCodeMapper.selectAll();
    }
}
