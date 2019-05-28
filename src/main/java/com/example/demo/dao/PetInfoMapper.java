package com.example.demo.dao;

import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.PetInfoDO;

import java.util.List;

/**
 * 宠物信息mapper
 *
 * @author zhou.xy
 * @since 1.0
 */
public interface PetInfoMapper extends BaseMapper<PetInfoDO> {

    int insertPet(PetInfoDO petInfoDO);

    int updatePet(PetInfoDO petInfoDO);

    List<PetInfoDO> selectByCondition(PetInfoDO petInfoDO);
}
