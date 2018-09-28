package com.example.demo.test;

import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 测试
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
public class MyTest {
    private static Pattern numberPattern = Pattern.compile("[0-9]+");
    public static void main(String[] args) {
        String str = "data:image/png;base64,iVBORw0KGgoAAAANSU";
        String str2 = str.replaceFirst("data:image/\\S*;base64,", "");
        log.info(str);
        log.info(str2);

        Long a = 123L;
        String b = "123";
        log.info(b.equals(a.toString()));
        log.info(Objects.equals(a, b));

        log.info(OptTypeEnum.getByCode("BIND2".replaceAll("\\d+","")).getMsg());

        log.info(numberPattern.matcher("BIND2").replaceAll(""));
    }

    public enum OptTypeEnum {
        OPT_UNBIND("UNBIND", "您已存在解绑操作"),
        OPT_BIND("BIND", "您已存在绑卡操作"),
        OPT_CHANGE("CHANGE", "您已存在换卡操作");
        private String code;
        private String msg;

        OptTypeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static OptTypeEnum getByCode(String code) {
            for (OptTypeEnum aCode : OptTypeEnum.values()) {
                if (aCode.getCode().equals(code)) {
                    return aCode;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }
}
