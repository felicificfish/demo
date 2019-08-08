package com.example.demo.controller.dto;

import com.example.demo.model.PetInfoDO;
import com.example.demo.service.BeanConvert;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 宠物信息
 *
 * @author zhou.xy
 * @since 1.0
 */
@Data
public class PetInfoDTO implements Serializable {
    @NotBlank
    private String nickname;

    public PetInfoDO convertToPetInfoDO() {
        PetInfoInputDTOConvert convert = new PetInfoInputDTOConvert();
        return convert.convert(this);
    }

    private static class PetInfoInputDTOConvert implements BeanConvert<PetInfoDTO, PetInfoDO> {
        @Override
        public PetInfoDO convert(PetInfoDTO petInfoDTO) {
            PetInfoDO petInfoDO = new PetInfoDO();
            BeanUtils.copyProperties(petInfoDTO, petInfoDO);
            return petInfoDO;
        }
    }

    private static class PetInfoConvert extends Converter<PetInfoDTO, PetInfoDO> {

        @Override
        protected PetInfoDO doForward(PetInfoDTO petInfoDTO) {
            PetInfoDO petInfoDO = new PetInfoDO();
            BeanUtils.copyProperties(petInfoDTO, petInfoDO);
            return petInfoDO;
        }

        @Override
        protected PetInfoDTO doBackward(PetInfoDO petInfoDO) {
            PetInfoDTO petInfoDTO = new PetInfoDTO();
            BeanUtils.copyProperties(petInfoDO, petInfoDTO);
            return petInfoDTO;
        }
    }
}
