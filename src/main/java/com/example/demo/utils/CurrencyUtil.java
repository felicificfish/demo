package com.example.demo.utils;

/**
 * 货币工具
 *
 * @author zhou.xy
 * @date 2018/9/20 17:34
 * @since 1.0
 */
public class CurrencyUtil {
    private CurrencyUtil() {
    }

    private static final String[] HanDigiStr = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static final String[] HanDiviStr = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
            "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};

    /**
     * 将货币转换为大写形式
     *
     * @param val 传入的数据
     * @return String 返回的人民币大写形式字符串
     */
    public static String numToRmbStr(double val) {
        String signStr = "";
        String tailStr;
        long fraction;
        long integer;
        int jiao;
        int fen;
        if (val < 0) {
            val = -val;
            signStr = "负";
        }
        if (val > 99999999999999.999 || val < -99999999999999.999) {
            return "数值位数过大!";
        }
        // 四舍五入到分
        long temp = Math.round(val * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;
        if (jiao == 0 && fen == 0) {
            tailStr = "整";
        } else {
            tailStr = HanDigiStr[jiao];
            if (jiao != 0) {
                tailStr += "角";
            }
            // 零元后不写零几分
            if (integer == 0 && jiao == 0) {
                tailStr = "";
            }
            if (fen != 0) {
                tailStr += HanDigiStr[fen] + "分";
            }
        }
        // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
        return signStr + positiveIntegerToHanStr(String.valueOf(integer)) + "元" + tailStr;
    }

    /**
     * 将货币转换为大写形式(类内部调用)<br>
     * 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
     *
     * @param numStr 货币
     * @return String
     */
    private static String positiveIntegerToHanStr(String numStr) {
        // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
        StringBuilder rmbStr = new StringBuilder();
        boolean lastzero = false;
        boolean hasvalue = false; // 亿、万进位前有数值标记
        int len;
        int n;
        len = numStr.length();
        if (len > 15) {
            return "数值过大!";
        }
        for (int i = len - 1; i >= 0; i--) {
            if (numStr.charAt(len - i - 1) == ' ') {
                continue;
            }
            n = numStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9) {
                return "输入含非数字字符!";
            }
            if (n != 0) {
                if (lastzero) {
                    rmbStr.append(HanDigiStr[0]);// 若干零后若跟非零值，只显示一个零
                }
                // 除了亿万前的零不带到后面
                // 如十进位前有零也不发壹音用此行
                if (!(n == 1 && (i % 4) == 1 && i == len - 1)) { // 十进位处于第一位不发壹音
                    rmbStr.append(HanDigiStr[n]);
                }
                rmbStr.append(HanDiviStr[i]); // 非零值后加进位，个位为空
                hasvalue = true; // 置万进位前有值标记
            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
                    rmbStr.append(HanDiviStr[i]); // “亿”或“万”
            }
            if (i % 8 == 0) {
                hasvalue = false; // 万进位前有值标记逢亿复位
            }
            lastzero = (n == 0) && (i % 4 != 0);
        }
        if (rmbStr.length() == 0) {
            return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        }
        return rmbStr.toString();
    }

}
