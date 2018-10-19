package com.example.demo.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 兑换码生成工具
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class RedeemCodeUtil {
    public static void main(String[] args) {
        String st1 = createBigSmallLetterStrOrNum(8);
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
    public static String createBigSmallLetterStrOrNum(int num) {

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

    /**
     * 生成len位的随机字符串集合(数字、大小写字母随机混排)
     *
     * @param history 历史集合
     * @param count   生成数量
     * @param len     字符串长度
     * @return
     */
    public static Set<String> createBigSmallLetterStrOrNum(Set<String> history, int count, int len) {
        Set<String> generatedCodes = new HashSet<String>(count * 4 / 3 + 1);
        if (history == null) {
            history = new HashSet<>(0);
        }
        while (generatedCodes.size() < count) {
            String code = createBigSmallLetterStrOrNum(len);
            if (!history.contains(code)) {
                generatedCodes.add(code);
            }
        }
        return generatedCodes;
    }
}
