package com.example.demo.wechat.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

/**
 * 微信工具类
 *
 * @author zhou.xy
 * @since 2019/8/27
 */
@Log4j2
public class WeChatUtil {
    /**
     * 微信开发者验证
     *
     * @param wxToken
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String wxToken, String signature, String timestamp, String nonce) {
        if (signature != null && timestamp != null && nonce != null) {
            String[] str = {wxToken, timestamp, nonce};
            // 字典序排序
            Arrays.sort(str);
            String bigStr = str[0] + str[1] + str[2];
            // SHA1加密
            String digest = EncoderHandler.encode("SHA1", bigStr).toLowerCase();
            // 确认请求来至微信
            if (digest.equals(signature)) {
                return true;
            }
        }
        return false;
    }
}
