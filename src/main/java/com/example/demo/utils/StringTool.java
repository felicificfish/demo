package com.example.demo.utils;

import com.example.demo.configs.mapper.code.Style;
import org.springframework.util.StringUtils;

/**
 * 字符串操作
 *
 * @author zhou.xy
 * @since 1.0
 */
public class StringTool {
    /**
     * 判断是否大写英文字母
     *
     * @param c 字符
     * @return 是否大写 true|false
     */
    public static boolean isUppercaseAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static char toUpperAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c = (char) (c - 32);
        }
        return c;
    }

    public static String camelhumpToUnderline(String str) {
        int size;
        char[] chars = str.toCharArray();
        size = chars.length;
        StringBuilder sb = new StringBuilder(size * 3 / 2 + 1);

        for (int i = 0; i < size; ++i) {
            char c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(c);
            } else {
                sb.append(toUpperAscii(c));
            }
        }

        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    public static String convertByStyle(String str, Style style) {
        switch (style) {
            case CAMELHUMP_SKIP_DO:
                String tableName = str.endsWith("DO") ? str.substring(0, str.length() - 2) : str;
                return camelhumpToUnderline(tableName);
            case CAMELHUMP:
                return camelhumpToUnderline(str);
            case UPPERCASE:
                return str.toUpperCase();
            case LOWERCASE:
                return str.toLowerCase();
            case CAMELHUMP_AND_LOWERCASE:
                return camelhumpToUnderline(str).toLowerCase();
            case CAMELHUMP_AND_UPPERCASE:
                return camelhumpToUnderline(str).toUpperCase();
            case NORMAL:
            default:
                return str;
        }
    }

    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName    用户名
     * @param replaceChar 替换字符
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName, String replaceChar) {
        if (userName == null) {
            return "*";
        }

        String userNameAfterReplaced;
        int nameLength = userName.length();
        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{0})\\d(?=\\d{1})", replaceChar);
        } else if (nameLength >= 3 && nameLength <= 6) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{1})\\d(?=\\d{1})", replaceChar);
        } else if (nameLength == 7) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{1})\\d(?=\\d{2})", replaceChar);
        } else if (nameLength == 8) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{2})\\d(?=\\d{2})", replaceChar);
        } else if (nameLength == 9) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{2})\\d(?=\\d{3})", replaceChar);
        } else if (nameLength == 10) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{3})\\d(?=\\d{3})", replaceChar);
        } else if (nameLength == 11) {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", replaceChar);
        } else {
            userNameAfterReplaced = userName.replaceAll("(?<=\\d{4})\\d(?=\\d{4})", replaceChar);
        }

        return userNameAfterReplaced;
    }

    /**
     * 字符串脱敏
     *
     * @param originalStr
     * @param replaceChar
     * @return
     */
    public static String replaceWithChar(String originalStr, String replaceChar, Integer left, Integer right) {
        if (StringUtils.isEmpty(originalStr)) {
            return null;
        }
        left = left == null ? 1 : left;
        right = right == null ? 1 : right;
        return originalStr.replaceAll("(?<=\\d{" + left + "})\\d(?=\\d{" + right + "})", replaceChar);
    }

    /**
     * 保留前四后四
     *
     * @param bankCard
     * @return
     */
    public static String hideBankCardNO(String bankCard) {
        return replaceWithChar(bankCard, "*", 4, 4);
    }

    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(replaceWithChar("6228480323012183510", "*", 3, null));
        System.out.println(replaceWithChar("6228480323012183510", "*", 3, 4));
        System.out.println(hideBankCardNO("6228480323012183510"));
        System.out.println(userNameReplaceWithStar("18693112345", "*"));
        System.out.println(org.apache.commons.lang3.StringUtils.rightPad(org.apache.commons.lang3.StringUtils.left("千里共婵娟", 1), "千里共婵娟".length(), "*"));

        String name = "千里共婵娟";
        System.out.println(name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(name.length() - 1)));
    }
}
