package com.example.demo.controller;

import com.example.demo.controller.vo.DrawInfoVO;
import com.example.demo.service.LuckDrawService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 幸运抽奖
 *
 * @author zhou.xy
 * @date 2019/8/14
 * @since 1.0
 */
@Log4j2
@RestController
public class LuckDrawController {
    @Autowired
    private LuckDrawService luckDrawService;

    @GetMapping("/draw/count")
    public DrawInfoVO getUserIntegralAndDrawCount(Integer activityType, Long userId) {
        return luckDrawService.queryUserDrawCountInfo(activityType, userId);
    }
}
