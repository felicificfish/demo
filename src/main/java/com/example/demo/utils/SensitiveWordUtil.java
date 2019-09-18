package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 敏感词工具类（基于DFA算法）
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
public class SensitiveWordUtil {
    /**
     * 只过滤最小敏感词
     */
    public static final Integer MATCH_TYPE_MIN = 1;
    /**
     * 过滤所有敏感词
     */
    public static final Integer MATCH_TYPE_ALL = 2;

    /**
     * 获取敏感词库
     *
     * @param
     * @return java.util.Map
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static Map<String, Object> getSensitiveWordMap() {
        SensitiveWordInit init = new SensitiveWordInit();
        return init.initSensitiveWord();
    }

    /**
     * 敏感词库敏感词数量
     *
     * @param
     * @return java.lang.Integer
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static Integer getSensitiveWordCount() {
        Map<String, Object> map = getSensitiveWordMap();
        if (CollectionUtils.isEmpty(map)) {
            return 0;
        }
        return map.size();
    }

    /**
     * 判断是否包含敏感词
     *
     * @param text
     * @param matchType
     * @return boolean
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static boolean isContainSensitiveWord(String text, Integer matchType) {
        boolean flag = false;
        for (int i = 0; i < text.length(); i++) {
            if (checkSensitiveWord(text, i, matchType) > 0) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取敏感词内容
     *
     * @param text
     * @param matchType
     * @return java.util.Set<java.lang.String>
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static Set<String> getSensitiveWord(String text, Integer matchType) {
        Set<String> sensitiveWordSet = new HashSet<>();
        for (int i = 0; i < text.length(); i++) {
            Integer len = checkSensitiveWord(text, i, matchType);
            if (len > 0) {
                sensitiveWordSet.add(text.substring(i, i + len));
                i = i + len - 1;
            }
        }
        return sensitiveWordSet;
    }

    /**
     * 替换敏感词
     *
     * @param text
     * @param matchType
     * @param replaceChar
     * @return java.lang.String
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static String replaceSensitiveWord(String text, Integer matchType, String replaceChar) {
        String resultTxt = text;
        Set<String> set = getSensitiveWord(text, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感词内容
     *
     * @param replaceChar
     * @param length
     * @return java.lang.String
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }
        return resultReplace;
    }

    /**
     * 检查敏感词数量
     *
     * @param text
     * @param beginIndex
     * @param matchType
     * @return java.lang.Integer
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static Integer checkSensitiveWord(String text, Integer beginIndex, Integer matchType) {
        // 敏感词结束标识位，用于敏感词只有1位的情况
        boolean flag = false;
        // 敏感词数量
        int matchFlag = 0;
        char word;
        Map<String, Object> sensitiveWordMap = getSensitiveWordMap();
        for (int i = beginIndex; i < text.length(); i++) {
            word = text.charAt(i);
            // 判断该字是否存在于敏感词库中
            sensitiveWordMap = (Map<String, Object>) sensitiveWordMap.get(Objects.toString(word));
            if (sensitiveWordMap == null) {
                break;
            }
            matchFlag++;
            // 判断是否是敏感词的结尾字，如果是结尾字则判断是否继续检测
            if ("1".equals(sensitiveWordMap.get("isEnd"))) {
                flag = true;
                // 判断过滤类型，如果是小过滤则跳出循环，否则继续循环
                if (MATCH_TYPE_MIN.equals(matchType)) {
                    break;
                }
            }
        }
        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }
}
