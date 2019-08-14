
package com.example.demo.dao;

import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.PrizeConfigDO;

import java.util.List;

/**
 * 奖品信息CRUD
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
public interface PrizeConfigMapper extends BaseMapper<PrizeConfigDO> {
    int insertOne(PrizeConfigDO prizeConfig);

    int insertBatch(List<PrizeConfigDO> list);

    int update(PrizeConfigDO prizeConfig);

    int updateBatch(List<PrizeConfigDO> list);

    PrizeConfigDO queryById(Long id);

    List<PrizeConfigDO> query(PrizeConfigDO prizeConfigDO);

    int queryCount(PrizeConfigDO prizeConfigDO);
}
