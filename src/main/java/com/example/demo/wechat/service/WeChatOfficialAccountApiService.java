package com.example.demo.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.configs.exception.ValidateException;
import com.example.demo.dao.WechatOfficialAccountUserMapper;
import com.example.demo.model.WechatOfficialAccountUserDO;
import com.example.demo.utils.RedisTemplateUtil;
import com.example.demo.wechat.model.WeChatTemplateMsg;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * 微信公众号api服务
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
@Log4j2
@Service
public class WeChatOfficialAccountApiService {
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static final String ACCESS_TOKEN_CACHE_KEY = "wechat_access_token_";
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.appSecret}")
    private String appSecret;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WechatOfficialAccountUserMapper wechatOfficialAccountUserMapper;

    /**
     * 获取token
     *
     * @return
     */
    public String getWeChatAccessToken() {
        String key = ACCESS_TOKEN_CACHE_KEY + appId;
        String accessToken = null;
        JSONObject jsonObject = RedisTemplateUtil.get(key);
        if (null != jsonObject) {
            return jsonObject.getString("access_token");
        }
        try {
            String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
            jsonObject = restTemplate.getForObject(requestUrl, JSONObject.class);
//            // 记录交互日志
//            WeChatOfficialApiDO weChatOfficialApiDO = new WeChatOfficialApiDO(1, requestUrl, "",
//                    null == jsonObject ? "" : jsonObject.toJSONString());
//            weChatOfficialApiService.asyncSave(weChatOfficialApiDO);
            if (jsonObject != null) {
                accessToken = jsonObject.getString("access_token");
                if (!StringUtils.isEmpty(accessToken)) {
                    // 微信token有效期是2小时
                    RedisTemplateUtil.set(key, jsonObject, 7000);
                    return accessToken;
                } else {
                    log.error("gain wechat access_token fail -- message:{}", jsonObject.toJSONString());
                }
            } else {
                log.error("gain wechat access_token  fail");
            }
        } catch (Exception e) {
            log.error("gain wechat access_token  fail---{}", e.getMessage());
        }
        return accessToken;
    }

    /**
     * 发送模板信息
     *
     * @param templateMsg 模板消息，封装示例： <br>
     *                    模板：尊敬的{{result.DATA}}: 恭喜您成功绑定聚优财帐号 领奖时间:{{withdrawTime.DATA}} <br>
     *                    WeChatTemplateMsg templateMsg = new WeChatTemplateMsg(); <br>
     *                    templateMsg.setTemplate_id(templateId);// 模板ID <br>
     *                    templateMsg.setTouser(openId);// 收件人 <br>
     *                    templateMsg.setUrl("https://www.jyc99.com");// 点击跳转地址 <br>
     *                    <p>
     *                    Map<String, WeChatTemplateData> templateData = new HashMap<>(); <br>
     *                    WeChatTemplateData result = new WeChatTemplateData(); <br>
     *                    result.setValue("***"); <br>
     *                    result.setColor("#173177"); <br>
     *                    templateData.put("result", result); <br>
     *                    <p>
     *                    WeChatTemplateData withdrawTime = new WeChatTemplateData(); <br>
     *                    withdrawTime.setValue(DateUtil.getDate()); <br>
     *                    withdrawTime.setColor("#173177"); <br>
     *                    templateData.put("withdrawTime", withdrawTime); <br>
     *                    <p>
     *                    templateMsg.setData(templateData);
     * @returnsendTemplateMsg
     */
    public JSONObject sendTemplateMsg(WeChatTemplateMsg templateMsg) {
        String sendTemplateMsgUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",
                getWeChatAccessToken());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            String param = JSON.toJSONString(templateMsg);
            HttpEntity<String> request = new HttpEntity<>(param, headers);
            JSONObject jsonObject = restTemplate.postForObject(sendTemplateMsgUrl, request, JSONObject.class);
            // 记录交互日志
//            WeChatOfficialApiDO weChatOfficialApiDO = new WeChatOfficialApiDO(3,
//                    sendTemplateMsgUrl, param, null == jsonObject ? "" : jsonObject.toJSONString());
//            weChatOfficialApiService.asyncSave(weChatOfficialApiDO);
            return jsonObject;
        } catch (Exception e) {
            log.error("发送模板消息异常，参数：{}，异常消息：{}", JSON.toJSONString(templateMsg), e.getMessage());
        }
        return new JSONObject();
    }

    /**
     * 获取公众号关注用户信息，并存入数据库
     *
     * @param openId 用户的标识，对当前公众号唯一
     */
    @Transactional(rollbackFor = Exception.class)
    public void getUserInfoAndSave(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return;
        }
        log.info("【{}】关注微信公众号，获取其相关信息", openId);
        String getUserInfoUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                getWeChatAccessToken(), openId);
        JSONObject jsonObject = restTemplate.getForObject(getUserInfoUrl, JSONObject.class);
//        // 记录交互日志
//        WeChatOfficialApiDO weChatOfficialApiDO = new WeChatOfficialApiDO(2, getUserInfoUrl, "", null == jsonObject ? "" : jsonObject.toJSONString());
//        weChatOfficialApiService.asyncSave(weChatOfficialApiDO);
        if (null == jsonObject) {
            log.warn("关注微信公众号获取用户信息未获取到返回结果");
            return;
        }
        if (null != jsonObject.get("errcode")) {
            log.error("关注微信公众号获取用户信息异常,{}", jsonObject.getString("errmsg"));
            throw new ValidateException(jsonObject.getString("errmsg"));
        }
        WechatOfficialAccountUserDO userDO = new WechatOfficialAccountUserDO();
        userDO.setOpenid(jsonObject.getString("openid"));
        userDO.setUnionid(jsonObject.getString("unionid"));
        userDO.setAppId(appId);
        userDO.setSubscribe(jsonObject.getInteger("subscribe"));
        // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
        Long subscribeTime = jsonObject.getLong("subscribe_time");
        if (null != subscribeTime) {
            userDO.setSubscribeTime(new Date(subscribeTime * 1000));
        }
        userDO.setNickname(jsonObject.getString("nickname"));
        userDO.setSex(jsonObject.getInteger("sex"));
        userDO.setCountry(jsonObject.getString("country"));
        userDO.setProvince(jsonObject.getString("province"));
        userDO.setCity(jsonObject.getString("city"));
        userDO.setLanguage(jsonObject.getString("language"));
        userDO.setHeadimgurl(jsonObject.getString("headimgurl"));
        userDO.setRemark(jsonObject.getString("remark"));
        userDO.setGroupid(jsonObject.getLong("groupid"));
        JSONArray tagidList = jsonObject.getJSONArray("tagid_list");
        if (null != tagidList && tagidList.size() > 0) {
            userDO.setTagidList(tagidList.toString());
        }
        userDO.setSubscribeScene(jsonObject.getString("subscribe_scene"));
        userDO.setQrScene(jsonObject.getString("qr_scene"));
        userDO.setQrSceneStr(jsonObject.getString("qr_scene_str"));

        int count = wechatOfficialAccountUserMapper.selectCountByPrimaryKey(jsonObject.getString("openid"));
        if (count > 0) {
            // 更新
            userDO.setModifiedon(new Date());
            wechatOfficialAccountUserMapper.update(userDO);
        } else {
            // 新增
            userDO.setCreatedon(new Date());
            wechatOfficialAccountUserMapper.insertOne(userDO);
        }
    }

    /**
     * 获取用户基本信息
     *
     * @param openId
     * @return com.alibaba.fastjson.JSONObject
     * @author zhou.xy
     * @date 2019/8/15
     * @since 1.0
     */
    public JSONObject userInfo(String openId) {
        String batchGetUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                getWeChatAccessToken(), openId);
        try {
            return restTemplate.getForObject(batchGetUrl, JSONObject.class);
        } catch (Exception e) {
            log.error("获取用户基本信息异常，异常消息：{}", e.getMessage());
        }
        return new JSONObject();
    }

    /**
     * 创建自定义菜单
     *
     * @param menus
     * @return com.alibaba.fastjson.JSONObject
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    public JSONObject menuCreate(String menus) {
        if (StringUtils.isEmpty(menus)) {
            JSONObject errorObj = new JSONObject();
            errorObj.put("errcode", "99999");
            errorObj.put("errmsg", "没有可发布的菜单");
            return errorObj;
        }
        String menuCreateUrl = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s",
                getWeChatAccessToken());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());

            HttpEntity<String> request = new HttpEntity<>(menus, headers);
            JSONObject jsonObject = restTemplate.postForObject(menuCreateUrl, request, JSONObject.class);
            // 记录交互日志
//            WeChatOfficialApiDO weChatOfficialApiDO = new WeChatOfficialApiDO(3,
//                    sendTemplateMsgUrl, param, null == jsonObject ? "" : jsonObject.toJSONString());
//            weChatOfficialApiService.asyncSave(weChatOfficialApiDO);
            return jsonObject;
        } catch (Exception e) {
            log.error("创建自定义菜单异常，参数：{}，异常消息：{}", menus, e.getMessage());
        }
        return new JSONObject();
    }

    /**
     * 删除自定义菜单
     *
     * @param
     * @return com.alibaba.fastjson.JSONObject
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public JSONObject menuDelete() {
        String menuDeleteUrl = String.format("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s",
                getWeChatAccessToken());
        try {
            return restTemplate.getForObject(menuDeleteUrl, JSONObject.class);
        } catch (Exception e) {
            log.error("删除自定义菜单异常，异常消息：{}", e.getMessage());
        }
        return new JSONObject();
    }
}
