package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.configs.api.JSONResultDO;
import com.example.demo.model.Student1;
import com.example.demo.model.Student2;
import com.example.demo.service.MultithreadingService;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    public JSONResultDO getMsg() {
        JSONResultDO result = new JSONResultDO(false, "error_msg");
        return result;
    }

    @GetMapping(value = "multithreading")
    public String Multithreading(@RequestParam String name) {
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
        byte[] conbyte = text.getBytes();
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < 4; j++) {
                    conbyte[i + j] = 0x30;
                }
                i += 3;
            }
        }
        String newText = new String(conbyte);
        log.info("============={}", newText);

        String a = new String(conbyte, StandardCharsets.UTF_8);
        log.info("----------------{}", a);


        String a1 = EmojiParser.parseToAliases(text);// 将表情符号转为字符
        String a2 = EmojiParser.parseToUnicode(a1);// 将字符转为表情符号
        log.info("a1={}, a2={}", a1, a2);

        return a2;
    }

    @PostMapping(value = "base64")
    public void base64Process(@RequestParam String base64Str) {
        base64Str = base64Str.replaceFirst("data:image/\\S*;base64,", "");
        byte[] bytes = Base64.decodeBase64(base64Str);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {// 调整异常数据
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

}
