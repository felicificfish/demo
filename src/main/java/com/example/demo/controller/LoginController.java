package com.example.demo.controller;

import com.example.demo.controller.vo.UserInfoVO;
import com.example.demo.controller.vo.UserLoginInfoVO;
import com.example.demo.controller.vo.UserSettingInfoVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @author zhou.xy
 * @date 2019/8/27
 * @since 1.0
 */
@RestController
public class LoginController {
    @Value("${cas.jwt-exp-millis}")
    private long jwtExpMillis;
    @Value("${cas.jwt-secretkey}")
    private String jwtSecret;

    /**
     * 用户登录
     *
     * @param request
     * @param response
     * @param userMobile   登录手机号
     * @param userPassword 登录密码
     * @param type         用户类型
     * @return com.example.demo.controller.vo.UserLoginInfoVO
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @PostMapping(value = "/user/login")
    public UserLoginInfoVO loginAjax(HttpServletRequest request, HttpServletResponse response,
                                     String userMobile, String userPassword, Integer type) {
        UserLoginInfoVO userLoginInfoVO = new UserLoginInfoVO();
        userLoginInfoVO.setUserToken(createJWT("1", null));
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserName("哪咤");
        userLoginInfoVO.setUserInfo(userInfoVO);
        return userLoginInfoVO;
    }

    /**
     * 获取个人信息
     *
     * @param
     * @return com.example.demo.controller.vo.UserSettingInfoVO
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @GetMapping(value = "/user/info")
    public UserSettingInfoVO userSettingInfo() {
        UserSettingInfoVO settingInfo = new UserSettingInfoVO();
        settingInfo.setUserName("哪咤");
        settingInfo.setMobile("18605818888");
        settingInfo.setSlogan("赚大钱，come on赚大钱，come on");
        settingInfo.setPhoto("https://jyc-investment-test.oss-cn-hangzhou.aliyuncs.com/VYLTqXsoPLfdFv3kK5HioUEoC63pWPXy.png");
        settingInfo.setQrCode("https://jyc-investment-test.oss-cn-hangzhou.aliyuncs.com/3s6VgGErtiEVlqIODNPSMzUF7WdZmgno.png");
        settingInfo.setPhotoH5("https://jyc-investment-test.oss-cn-hangzhou.aliyuncs.com/DGlcfs9NHsSDHE59tcfAYrNOy68Hl7Zi.png");
        return settingInfo;
    }

    /**
     * 创建jwt
     *
     * @param userId 用户ID
     * @param claims 创建payload的私有声明（根据特定的业务需要添加，可以拿这个做验证）
     * @return java.lang.String
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    public String createJWT(String userId, Map<String, String> claims) {
        // 未配置过期时间，默认30天
        if (jwtExpMillis == 0) {
            jwtExpMillis = 30 * 24 * 60 * 60 * 1000L;
        }
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpMillis);

        Map<String, Object> clmsMap = new HashMap<>(1);
        clmsMap.put("datas", claims);

        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己的私有的声明，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(clmsMap)
                // JWT的唯一标识，可以设置为一个不重复的值,从而回避重放攻击。
                .setId(RandomStringUtils.randomNumeric(16))
                // jwt的签发时间
                .setIssuedAt(now)
                // 设置过期时间
                .setExpiration(exp)
                // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串。
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, generalKey());
        return builder.compact();
    }

    private SecretKey generalKey() {
        byte[] encodedKey = jwtSecret.getBytes();
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
