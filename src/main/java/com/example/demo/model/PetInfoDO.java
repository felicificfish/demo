package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 宠物信息
 *
 * @author zhou.xy
 * @date 2018/9/21 15:19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "pet_info")
public class PetInfoDO implements Serializable {
    private Long id;
    private String nickname;
    private Date ctime;
}
