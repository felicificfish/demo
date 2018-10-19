package com.example.demo.utils;

import java.util.Random;

/**
 * 兑换码生成工具
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class RedeemCodeUtil {
    public static void main(String[] args) {
        String st1 = createBigSmallLetterStrOrNumberRadom(8);
        String st2 = createSmallStrOrNumberRadom(8);
        String st3 = createBigStrOrNumberRadom(8);
        System.out.println(st1);
        System.out.println(st2);
        System.out.println(st3);
    }

    /**
     * 生成num位的随机字符串(数字、大小写字母随机混排)
     *
     * @param num 位数
     * @return String
     */
    public static String createBigSmallLetterStrOrNumberRadom(int num) {

        String str = "";
        for (int i = 0; i < num; i++) {
            int intVal = (int) (Math.random() * 58 + 65);
            if (intVal >= 91 && intVal <= 96) {
                i--;
            }
            if (intVal < 91 || intVal > 96) {
                if (intVal % 2 == 0) {
                    str += (char) intVal;
                } else {
                    str += (int) (Math.random() * 10);
                }
            }
        }
        return str;
    }

    /**
     * 生成num位的随机字符串(数字 、 小写字母随机混排)
     *
     * @param num 位数
     * @return String
     */
    public static String createSmallStrOrNumberRadom(int num) {
        String str = "";
        for (int i = 0; i < num; i++) {
            int intVal = (int) (Math.random() * 26 + 97);
            if (intVal % 2 == 0) {
                str += (char) intVal;
            } else {
                str += (int) (Math.random() * 10);
            }
        }
        return str;
    }

    /**
     * 生成num位的随机字符串(小写字母与数字混排)
     *
     * @param num 位数
     * @return String
     */
    public static String createBigStrOrNumberRadom(int num) {

        String str = "";
        for (int i = 0; i < num; i++) {
            int intVal = (int) (Math.random() * 26 + 65);
            if (intVal % 2 == 0) {
                str += (char) intVal;
            } else {
                str += (int) (Math.random() * 10);
            }
        }
        return str;
    }
}
