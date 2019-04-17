package com.example.demo.utils;

import com.example.demo.utils.encode.DigestUtil;
import org.springframework.util.DigestUtils;

/**
 * TODO
 *
 * @author zhou.xy
 * @since 2019/4/17
 */
public class MD5Util {
    /**
     * MD5盐值字符串，用于混淆
     */
    private static final String salt = "sdfㄋ∫h^de&$ㄖ454□13οΑΒ∥αㄊβτξμυ－τμдггссㄗ∏!23⊥№§＆χχ＠↑♂◎♀";

    public static String getMD5(String str) {
        String base = str + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
        System.out.println(DigestUtil.shaDigestAsHex("123456".getBytes()));
    }
}
