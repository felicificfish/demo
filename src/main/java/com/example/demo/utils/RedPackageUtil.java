package com.example.demo.utils;

import com.example.demo.model.RedPackageDO;

import java.util.Random;

/**
 * 抢红包工具
 *
 * @author zhou.xy
 * @since 2019/3/28
 */
public class RedPackageUtil {
    public static double getRandomMoney(RedPackageDO redPackageDO) {
        if (redPackageDO.getRemainSize() == 1) {
            RedisTemplateUtil.delete("redPackage");
            redPackageDO.setRemainSize(redPackageDO.getRemainSize() - 1);
            return (double) Math.round(redPackageDO.getRemainMoney() * 100) / 100;
        }
        Random r = new Random();
        double min = 0.01;
        double max = redPackageDO.getRemainMoney() / redPackageDO.getRemainSize() * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01 : money;
        money = Math.floor(money * 100) / 100;
        redPackageDO.setRemainSize(redPackageDO.getRemainSize() - 1);
        redPackageDO.setRemainMoney(redPackageDO.getRemainMoney() - money);
        RedisTemplateUtil.set("redPackage", redPackageDO);
        return money;
    }
}
