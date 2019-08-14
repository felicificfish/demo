
package com.example.demo.dao;

import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.ActivityTimeDO;

import java.util.List;

/**
 * 活动时间CRUD
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
public interface ActivityTimeMapper extends BaseMapper<ActivityTimeDO> {
    int insertOne(ActivityTimeDO activityTime);

    int insertBatch(List<ActivityTimeDO> list);

    int update(ActivityTimeDO activityTime);

    int updateBatch(List<ActivityTimeDO> list);

    ActivityTimeDO queryById(Long id);

    List<ActivityTimeDO> query(ActivityTimeDO activityTimeDO);

    int queryCount(ActivityTimeDO activityTimeDO);
}
