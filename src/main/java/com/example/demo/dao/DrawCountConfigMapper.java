
package com.example.demo.dao;


import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.DrawCountConfigDO;

import java.util.List;

/**
 * 抽奖次数获取配置CRUD
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
public interface DrawCountConfigMapper extends BaseMapper<DrawCountConfigDO> {
    int insertOne(DrawCountConfigDO drawCountConfig);

    int insertBatch(List<DrawCountConfigDO> list);

    int update(DrawCountConfigDO drawCountConfig);

    int updateBatch(List<DrawCountConfigDO> list);

    DrawCountConfigDO queryById(Long id);

    List<DrawCountConfigDO> query(DrawCountConfigDO drawCountConfigDO);

    int queryCount(DrawCountConfigDO drawCountConfigDO);
}
