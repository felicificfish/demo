package com.example.demo.service;

import com.example.demo.dao.SensitiveWordMapper;
import com.example.demo.model.SensitiveWordDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词服务
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
@Service
public class SensitiveWordService {
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    /**
     * 查询所有敏感词信息
     *
     * @param
     * @return java.util.List<com.example.demo.model.SensitiveWordDO>
     * @since 1.0
     */
    public List<SensitiveWordDO> queryList() {
        // TODO 缓存
        long s = System.currentTimeMillis();
        List<SensitiveWordDO> sensitiveWordList = sensitiveWordMapper.selectAll();
        long e = System.currentTimeMillis();
        log.info("==================查询时间：{}秒", (e - s) / 1000);
        return sensitiveWordList;
    }
}
