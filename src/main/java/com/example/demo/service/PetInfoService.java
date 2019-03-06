package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.dao.PetInfoMapper;
import com.example.demo.model.PetInfoDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宠物信息Service
 *
 * @author zhou.xy
 * @date 2018/9/21 15:22
 * @since 1.0
 */
@Log4j2
@Service
public class PetInfoService {
    @Autowired
    private PetInfoMapper petInfoMapper;

    public PetInfoDO queryPet(PetInfoDO petInfo) {
        Example example = new Example(PetInfoDO.class);
        example.createCriteria().andEqualTo("id", petInfo.getId());
        List<PetInfoDO> list = petInfoMapper.selectByExample(example);
        log.info("查询结果：{}", JSON.toJSONString(list));

        return petInfoMapper.selectOne(petInfo);
    }

    public List<PetInfoDO> queryAll() {
        return petInfoMapper.selectAll();
    }

    public int addPetInfo(PetInfoDO petInfoDO) {
        return petInfoMapper.insertPet(petInfoDO);
    }

    public int updatePetInfo(PetInfoDO petInfoDO) {
        return petInfoMapper.updatePet(petInfoDO);
    }
}
