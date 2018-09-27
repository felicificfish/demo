package com.example.demo.test;

import java.util.Objects;
import java.util.regex.Pattern;

public class MyTest {
    public static void main(String[] args) {
        String str = "data:image/png;base64,iVBORw0KGgoAAAANSU";
        String str2 = str.replaceFirst("data:image/\\S*;base64,", "");
        System.out.println(str);
        System.out.println(str2);

        Long a = 123L;
        String b = "123";
        System.out.println(b.equals(a.toString()));
        System.out.println(Objects.equals(a, b));

        System.out.println(OptTypeEnum.getByCode("BIND2".replaceAll("\\d+","")).getMsg());

        System.out.println(Pattern.compile("[^0-9]").matcher("BIND2").replaceAll(""));
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

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
