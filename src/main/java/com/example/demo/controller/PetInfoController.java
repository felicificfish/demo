package com.example.demo.controller;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.controller.dto.PetInfoDTO;
import com.example.demo.model.PetInfoDO;
import com.example.demo.model.PetInfoVO;
import com.example.demo.service.PetInfoService;
import com.example.demo.utils.ExcelExportUtil;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.SqlMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private SqlSession sqlSession;

    @GetMapping(value = "petInfo")
    public PetInfoDO petInfo() {
        PetInfoDO petInfo = new PetInfoDO();
        petInfo.setId(1L);
        return petInfoService.queryPet(petInfo);
    }

    @GetMapping(value = "petInfoOne")
    public PetInfoDO petInfoOne() {
        SqlMapper sqlExcutor = new SqlMapper(sqlSession);
        return sqlExcutor.selectOne("select * from pet_info limit 1", PetInfoDO.class);
    }

    /**
     * 导出宠物数据模板
     *
     * @param response 响应
     * @param request  请求
     */
    @GetMapping(value = "exportTmp")
    public void export(HttpServletResponse response, HttpServletRequest request) {
        String name = "宠物";
        ExportParams exportParams = new ExportParams(null, name, ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, PetInfoVO.class, new ArrayList<>());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            FileUtil.response(request, response, content, name + ".xls");
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @GetMapping("addPet")
    public void addPetInfo(String nickname) {
        PetInfoDO petInfoDO = new PetInfoDO();
        petInfoDO.setNickname(nickname);
        petInfoDO.setCtime(new Date());
        petInfoService.addPetInfo(petInfoDO);
    }

    @GetMapping("addPet2")
    public void addPetInfo(@Valid PetInfoDTO petInfoDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            List<String> errorMsgList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(allErrors)) {
                for (FieldError error : allErrors) {
                    errorMsgList.add(error.getField() + error.getDefaultMessage());
                }
            }
            throw new ValidateException(StringUtils.join(errorMsgList, ","));
        }
        PetInfoDO petInfoDO = petInfoDTO.convertToPetInfoDO();
        petInfoDO.setCtime(new Date());
        petInfoService.addPetInfo(petInfoDO);
    }

    @GetMapping("updatePet")
    public void addPetInfo(Long id, String nickname) {
        PetInfoDO petInfoDO = new PetInfoDO();
        petInfoDO.setNickname(nickname);
        petInfoDO.setId(id);
        petInfoService.updatePetInfo(petInfoDO);
    }

    @GetMapping("petList")
    public List<PetInfoDO> petList(PetInfoDO petInfoDO) {
        return petInfoService.query(petInfoDO);
    }
}
