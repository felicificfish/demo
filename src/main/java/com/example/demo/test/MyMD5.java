package com.example.demo.test;

import com.example.demo.utils.encode.DigestUtil;

import java.util.regex.Pattern;

/**
 * 加解密
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class MyMD5 {
    public static void main(String[] args) {
        System.out.println(DigestUtil.shaDigestAsHex("123456@JYC.com".getBytes()));
    }

}
