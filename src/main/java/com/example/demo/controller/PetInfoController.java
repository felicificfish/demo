package com.example.demo.controller;

import com.example.demo.model.PetInfoDO;
import com.example.demo.service.PetInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宠物信息Controller
 *
 * @author zhou.xy
 * @since 1.0
 */
@Log4j2
@RestController
public class PetInfoController {
    @Autowired
    private PetInfoService petInfoService;

    @GetMapping(value = "petInfo")
    public PetInfoDO petInfo() {
        PetInfoDO petInfo = new PetInfoDO();
        petInfo.setId(1L);
        return petInfoService.queryPet(petInfo);
    }
}
