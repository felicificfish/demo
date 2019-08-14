
package com.example.demo.dao;


import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.UserDrawRecordDO;

import java.util.List;

/**
 * 用户抽奖记录CRUD
 *
 * @author zhou.xy
 * @since 2019/8/14
 */
public interface UserDrawRecordMapper extends BaseMapper<UserDrawRecordDO> {
    int insertOne(UserDrawRecordDO userDrawRecord);

    int insertBatch(List<UserDrawRecordDO> list);

    int update(UserDrawRecordDO userDrawRecord);

    int updateBatch(List<UserDrawRecordDO> list);

    UserDrawRecordDO queryById(Long id);

    List<UserDrawRecordDO> query(UserDrawRecordDO userDrawRecordDO);

    int queryCount(UserDrawRecordDO userDrawRecordDO);
}
