package com.example.demo.model;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.util.Date;

/**
 * 宠物信息
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
@ExcelTarget("petInfoVO")
public class PetInfoVO {
    @Excel(name = "编号")
    private Long id;
    @Excel(name = "昵称")
    private String nickname;
    @Excel(name = "创建时间")
    private Date ctime;
}
