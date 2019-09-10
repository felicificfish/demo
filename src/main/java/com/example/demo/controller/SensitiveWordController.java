package com.example.demo.controller;

import com.example.demo.utils.SensitiveWordUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 敏感词测试
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
@RestController
public class SensitiveWordController {

    @GetMapping(value = "/sensitive")
    public String hello(String hello, Integer matchType, String replaceChar) {
        log.info(SensitiveWordUtil.isContainSensitiveWord(hello, matchType));
        log.info(SensitiveWordUtil.getSensitiveWord(hello, matchType));
        return SensitiveWordUtil.replaceSensitiveWord(hello, matchType, replaceChar);
    }
}
