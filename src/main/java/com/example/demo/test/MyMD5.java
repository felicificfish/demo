package com.example.demo.test;

import com.example.demo.utils.Patterns;
import com.example.demo.utils.ValidateUtil;
import com.example.demo.utils.encode.DigestUtil;

import java.util.regex.Matcher;
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

        System.out.println(ValidateUtil.isUrl("https://www.research.ibm.com/nanoscience/nanotubes/dfsd?aaa=你好"));

        String content = "F:\\第七https://www.abc.cn 届西部 http://www.baidu.com/abc=234 锅炉展电子版会刊 http://www.jyc99.com";
        Matcher matcher = Patterns.WEB_URL.matcher(content);
        if (matcher.find()){
            System.out.println(matcher.group());
        }

        System.out.println();

        //只提取http://或https://开头的
        Pattern p = Pattern.compile("(?<!\\d)(?:(?:[a-zA-z]+://[^\\s|^\\u4e00-\\u9fa5]*))(?!\\d)");
        Matcher m = p.matcher(content);
        while (m.find()) {
            System.out.println(m.group());
        }
//        Pattern pb = Pattern.compile("(?<!\\d)(?:(?:[\\w[.-://]]*\\.[com|cn|net|tv|gov|org|biz|cc|uk|jp|edu]+[^\\s|^\\u4e00-\\u9fa5]*))");
//        Matcher mb = pb.matcher(content);
//        while (mb.find()) {
//            System.out.println(mb.group());
//        }
    }

}
