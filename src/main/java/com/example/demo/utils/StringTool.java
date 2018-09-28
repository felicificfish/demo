package com.example.demo.utils;

import com.example.demo.configs.mapper.code.Style;

/**
 * 字符串操作
 *
 * @author zhou.xy
 * @since 1.0
 */
public class StringTool {

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
        char[] chars;
        StringBuilder sb = new StringBuilder((size = (chars = str.toCharArray()).length) * 3 / 2 + 1);

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
}
