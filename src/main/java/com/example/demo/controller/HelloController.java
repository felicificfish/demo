package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.configs.api.JsonResultDO;
import com.example.demo.constant.Constants;
import com.example.demo.model.RedPackageDO;
import com.example.demo.model.Student1;
import com.example.demo.model.Student2;
import com.example.demo.service.MultithreadingService;
import com.example.demo.utils.RedPackageUtil;
import com.example.demo.utils.RedisTemplateUtil;
import com.example.demo.utils.WebUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HelloController
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
@RestController
public class HelloController {
    @Value("${user.login.period:30}")
    private Integer period;// 获取配置信息，当取不到时使用:后的值

    @Autowired
    private MultithreadingService multithreadingService;

    @GetMapping(value = "hello")
    public String hello() {
        Student1 s1 = new Student1();
        s1.setId(123L);
        s1.setName("123");
        s1.setSex(1);
        Student2 s2 = new Student2();
        BeanUtils.copyProperties(s1, s2, "name");
        log.info("s1:{}", JSON.toJSONString(s1));
        log.info("s2:{}", JSON.toJSONString(s2));
        return "Hello Spring Boot!" + period;
    }

    @GetMapping(value = "getMsg")
    public JsonResultDO getMsg() {
        return new JsonResultDO(false, "error_msg");
    }

    @GetMapping(value = "multithreading")
    public String multiThreading(@RequestParam String name) {
        List<String> names = new ArrayList<>();
        names.add(name);
        names.add("111");
        names.add("222");
        names.add("333");
        return multithreadingService.multithreading(names);

    }

    @GetMapping(value = "specialchar")
    public String specialChar(@RequestParam String text) {
        // 四字节字符处理
        log.info("============={}", text);
        byte[] conByte = text.getBytes();
        for (int i = 0; i < conByte.length; i++) {
            if ((conByte[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < Constants.Common.NUMBER_FOUR; j++) {
                    conByte[i + j] = 0x30;
                }
                i += 3;
            }
        }
        String newText = new String(conByte);
        log.info("============={}", newText);

        String a = new String(conByte, StandardCharsets.UTF_8);
        log.info("----------------{}", a);


        // 将表情符号转为字符
        String a1 = EmojiParser.parseToAliases(text);
        // 将字符转为表情符号
        String a2 = EmojiParser.parseToUnicode(a1);
        log.info("a1={}, a2={}", a1, a2);

        return a2;
    }

    @PostMapping(value = "base64")
    public void base64Process(@RequestParam String base64Str) {
        base64Str = base64Str.replaceFirst("data:image/\\S*;base64,", "");
        byte[] bytes = Base64.decodeBase64(base64Str);
        for (int i = 0; i < bytes.length; ++i) {
            // 调整异常数据
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        try (OutputStream out = new FileOutputStream("D:/b.png")) {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "base642")
    public void base64Process2(@RequestParam String base64Str) {
        base64Str = base64Str.replaceFirst("data:image/\\S*;base64,", "");
        byte[] bytes = java.util.Base64.getDecoder().decode(base64Str);
        for (int i = 0; i < bytes.length; ++i) {
            // 调整异常数据
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        try (OutputStream out = new FileOutputStream("D:/b.png")) {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "getIp")
    public Map<String, String> testIp(HttpServletRequest request) {
        Map<String, String> ips = new HashMap<>();
        ips.put("sweet", WebUtil.getRealIpAddr(request));
        ips.put("mine", WebUtil.getIP(request));
        return ips;
    }

    @GetMapping("dynamic/export")
    public void dynamicColumnExport(HttpServletResponse response) {
        try {
            List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity colEntity = new ExcelExportEntity("商品名称", "title");
            colEntity.setNeedMerge(true);
            colList.add(colEntity);

            colEntity = new ExcelExportEntity("供应商", "supplier");
            colEntity.setNeedMerge(true);
            colList.add(colEntity);

            ExcelExportEntity deliColGroup = new ExcelExportEntity("得力", "deli");
            List<ExcelExportEntity> deliColList = new ArrayList<>();
            deliColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            deliColList.add(new ExcelExportEntity("专区价", "salePrice"));
            deliColGroup.setList(deliColList);
            colList.add(deliColGroup);

            ExcelExportEntity jdColGroup = new ExcelExportEntity("京东", "jd");
            List<ExcelExportEntity> jdColList = new ArrayList<>();
            jdColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            jdColList.add(new ExcelExportEntity("专区价", "salePrice"));
            jdColGroup.setList(jdColList);
            colList.add(jdColGroup);


            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<>();
                valMap.put("title", "名称." + i);
                valMap.put("supplier", "供应商." + i);

                List<Map<String, Object>> deliDetailList = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    Map<String, Object> deliValMap = new HashMap<>();
                    deliValMap.put("orgPrice", "得力.市场价." + j);
                    deliValMap.put("salePrice", "得力.专区价." + j);
                    deliDetailList.add(deliValMap);
                }
                valMap.put("deli", deliDetailList);

                List<Map<String, Object>> jdDetailList = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    Map<String, Object> jdValMap = new HashMap<>();
                    jdValMap.put("orgPrice", "京东.市场价." + j);
                    jdValMap.put("salePrice", "京东.专区价." + j);
                    jdDetailList.add(jdValMap);
                }
                valMap.put("jd", jdDetailList);

                list.add(valMap);
            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode("价格分析表.xls", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("价格分析表", "数据"),
                    colList, list);
            OutputStream fos = response.getOutputStream();
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("dynamic/export2")
    public void dynamicColumnExport2(HttpServletResponse response) {
        try {
            List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity colEntity = new ExcelExportEntity("商品名称", "title");
            colList.add(colEntity);

            colEntity = new ExcelExportEntity("供应商", "supplier");
            colList.add(colEntity);

            colEntity = new ExcelExportEntity("市场价", "orgPrice");
            colList.add(colEntity);

            List<String> suppliers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                suppliers.add("供应商." + i);
            }
            List<String> orgPrices = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                orgPrices.add("市场价." + i);
            }
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<>();
                valMap.put("title", "名称." + i);
                if (i < suppliers.size()) {
                    valMap.put("supplier", suppliers.get(i));
                }
                if (i < orgPrices.size()) {
                    valMap.put("orgPrice", orgPrices.get(i));
                }
                list.add(valMap);
            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode("价格分析表.xls", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("价格分析表", "数据"),
                    colList, list);
            OutputStream fos = response.getOutputStream();
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("redPackage")
    public List<Double> getRandomMoney(Integer size, Double money) {
        RedisTemplateUtil.delete("redPackage");
        List<Double> list = new ArrayList<>();
        double total = 0;
        for (int i = 0; i < size; i++) {
            RedPackageDO redPackageDO = RedisTemplateUtil.get("redPackage");
            if (null == redPackageDO) {
                redPackageDO = new RedPackageDO(size, money);
            }
            double randomMoney = RedPackageUtil.getRandomMoney(redPackageDO);
            list.add(randomMoney);
            total += randomMoney;
        }
        list.add(total);
        return list;
    }
}
