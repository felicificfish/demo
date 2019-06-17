package com.example.demo.test;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.example.demo.model.GiftDO;
import com.example.demo.utils.LotteryUtil;
import com.example.demo.utils.NumberToCN;
import com.taobao.api.ApiException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    public static String APPKEY = "";
    public static String APPSECRET ="";
    public static Long AGENTID = 0L;

    public static void main(String[] args) throws Exception {
//        String str = "data:image/png;base64,iVBORw0KGgoAAAANSU";
//        String str2 = str.replaceFirst("data:image/\\S*;base64,", "");
//        log.info(str);
//        log.info(str2);
//
//        Long a = 123L;
//        String b = "123";
//        log.info(b.equals(a.toString()));
//        log.info(Objects.equals(a, b));
//
//        log.info(OptTypeEnum.getByCode("BIND2".replaceAll("\\d+", "")).getMsg());
//
//        log.info(numberPattern.matcher("BIND2").replaceAll(""));
//
//        lottery();

//        doBatch();

//        System.out.println(NumberToCN.number2CNMontrayUnit(BigDecimal.valueOf(12323324.2323)));
//        System.out.println(NumberToCN.formatCNDecimal(BigDecimal.valueOf(12323324.2323)));
        //发送消息
//        String content = "{"
//                + "\"touser\": \"manager5937\","//发送用户ID，多个用,分割
//                + "\"toparty\": \"\","//发送部门ID，多个用,分割
//                + "\"agentid\": \""+"270146573"+"\","
//                + "\"msgtype\": \"text\","
//                + "\"text\": {\"content\": \"***(用户姓名) 电话***(用户电话)，在***(预约单创建时间)提交了预约申请，预约金额为***，请及时跟进。\"}"
//                + "}";
//        String url = "https://oapi.dingtalk.com/message/send?access_token="+getToken();
//        String rt = httpsRequest(url, "GET", content);
//        System.out.println(rt);

//        String roles = getRoles();
//        log.info(roles);

//        log.info(getManagerRoleId());

//        log.info(getDepartmentUser());

//        log.info(getDepartmentUserDetail());

//        sendWorkMarkdownMsg("客户预约通知", "诸葛亮 电话13811112356，在2019-06-14 18:57:20提交了预约申请，预约金额为2000万元，请及时跟进。", "130534255921758252");

//        getCheckInRecord();
        workRecord();
    }

    public static String getToken() throws RuntimeException {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();

            request.setAppkey(APPKEY);
            request.setAppsecret(APPSECRET);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            return accessToken;
        } catch (ApiException e) {
            log.error("getAccessToken failed", e);
            throw new RuntimeException();
        }

    }

    public static String getRoles() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/list");
        OapiRoleListRequest request = new OapiRoleListRequest();
        request.setOffset(0L);
        request.setSize(10L);

        OapiRoleListResponse response = client.execute(request, getToken());
        return JSON.toJSONString(response);
    }

    public static String getManagerRoleId() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");
        OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
        request.setRoleId(459467150L);
        request.setOffset(0L);
        request.setSize(10L);

        OapiRoleSimplelistResponse response = client.execute(request, getToken());
        return JSON.toJSONString(response);
    }

    public static String getDepartmentUser() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");
        OapiUserSimplelistRequest request = new OapiUserSimplelistRequest();
        request.setDepartmentId(1L);
        request.setOffset(0L);
        request.setSize(10L);
        request.setHttpMethod("GET");

        OapiUserSimplelistResponse response = client.execute(request, getToken());
        return JSON.toJSONString(response);
    }

    public static String getDepartmentUserDetail() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        request.setDepartmentId(1L);
        request.setOffset(0L);
        request.setSize(10L);
        request.setOrder("entry_desc");
        request.setHttpMethod("GET");
        OapiUserListbypageResponse response = client.execute(request,getToken());
        return JSON.toJSONString(response);
    }

    private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
          HttpsURLConnection conn = null;
          BufferedReader bufferedReader = null;
          try {
              URL url = new URL(requestUrl);
              conn = (HttpsURLConnection) url.openConnection();
              conn.setDoOutput(true);
              conn.setDoInput(true);
              conn.setUseCaches(false);
              conn.setRequestMethod(requestMethod);
              conn.setRequestProperty("content-type", "application/json");
              if (null != outputStr) {
                   OutputStream outputStream = conn.getOutputStream();
                   outputStream.write(outputStr.getBytes("utf-8"));
                   outputStream.close();
              }
              bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
              String str = null;
              StringBuffer buffer = new StringBuffer();
              while ((str = bufferedReader.readLine()) != null) {
                   buffer.append(str);
              }
              return buffer.toString();
          } catch (Exception e) {
              throw e;
          } finally {
              if (conn != null) {
                   conn.disconnect();
              }
              if (bufferedReader != null) {
                   try {
                        bufferedReader.close();
                   } catch (IOException e) {

                   }
              }
          }
     }

    /**
     * 发送工作通知消息
     *
     * @param content
     * @param userIdList 可选	接收者的用户userid列表，最大列表长度：100，多个以英文半角逗号隔开，如：zhangsan,lisi
     */
    public static void sendWorkMsg(String content, String userIdList) {
        if (StringUtils.isEmpty(userIdList)) {
            return;
        }
        //发送消息
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIdList);
        request.setAgentId(AGENTID);
        request.setToAllUser(false);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        OapiMessageCorpconversationAsyncsendV2Request.Text text = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        text.setContent(content);
        msg.setText(text);
        request.setMsg(msg);
        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, getToken());
            if (null == response || response.getErrcode() != 0) {
                log.error("Ding talk sent work msg failed! userIdList:{}, content:{}", userIdList, content);
            }
        } catch (ApiException e) {
            log.error("Ding talk sent work msg error! error:{} userIdList:{}, content:{}", e, userIdList, content);
        }
    }

    public static void sendWorkMarkdownMsg(String title, String content, String userIdList) {
        if (StringUtils.isEmpty(userIdList)) {
            return;
        }
        //发送消息
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIdList);
        request.setAgentId(AGENTID);
        request.setToAllUser(false);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setText(content);
        msg.getMarkdown().setTitle(title);
        request.setMsg(msg);
        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, getToken());
            if (null == response || response.getErrcode() != 0) {
                log.error("Ding talk sent work msg failed! userIdList:{}, content:{}", userIdList, content);
            }
        } catch (ApiException e) {
            log.error("Ding talk sent work msg error! error:{} userIdList:{}, content:{}", e, userIdList, content);
        }
    }

    public static void getCheckInRecord() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/checkin/record");
        OapiCheckinRecordRequest request = new OapiCheckinRecordRequest();
        request.setDepartmentId("1");
        request.setStartTime(System.currentTimeMillis()- TimeUnit.DAYS.toMillis(1));
        request.setEndTime(System.currentTimeMillis());
        request.setOffset(0L);
        request.setOrder("asc");
        request.setSize(100L);
        request.setHttpMethod("GET");
        try {
            OapiCheckinRecordResponse response = client.execute(request, getToken());
            log.info(JSON.toJSONString(response));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public static void workRecord() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        req.setUserid("130534255921758252");
        req.setCreateTime(System.currentTimeMillis());
        req.setTitle("请处理");
        req.setUrl("https://oa.dingtalk.com");
        List<OapiWorkrecordAddRequest.FormItemVo> list2 = new ArrayList<OapiWorkrecordAddRequest.FormItemVo>();
        OapiWorkrecordAddRequest.FormItemVo obj3 = new OapiWorkrecordAddRequest.FormItemVo();
        list2.add(obj3);
        obj3.setTitle("标题");
        obj3.setContent("内容");
        req.setFormItemList(list2);
        OapiWorkrecordAddResponse rsp = null;
        try {
            rsp = client.execute(req, getToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
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

    private static void lottery() {
        GiftDO iphone = new GiftDO();
        iphone.setId(101);
        iphone.setName("苹果手机");
        iphone.setProbability(0.1D);

        GiftDO thanks = new GiftDO();
        thanks.setId(102);
        thanks.setName("再接再厉");
        thanks.setProbability(0.5D);

        GiftDO vip = new GiftDO();
        vip.setId(103);
        vip.setName("优酷会员");
        vip.setProbability(0.4D);

        List<GiftDO> list = new ArrayList<GiftDO>();
        list.add(vip);
        list.add(thanks);
        list.add(iphone);

        for (int i = 0; i < 100; i++) {
            int index = LotteryUtil.lottery(list);
            log.info(JSON.toJSONString(list.get(index)));
        }
    }

    private static void doBatch() {
        List<String> dataList = new ArrayList<>();
        dataList.add("11");
        dataList.add("22");
        dataList.add("33");
        dataList.add("44");
        dataList.add("55");
        dataList.add("66");
        dataList.add("77");
        dataList.add("88");
        dataList.add("99");
        dataList.add("00");
        dataList.add("01");
        dataList.add("02");
        dataList.add("03");
        dataList.add("04");

        // 分批插入数据库
        int limitSize = 4;
        int size = dataList.size();
//        if (size > limitSize) {
//            int batch = size / limitSize;
//            for (int i = 0; i < batch; i++) {
//                System.out.println("第" + (i + 1) + "批：" + dataList.subList(0, limitSize));
//                dataList.subList(0, limitSize).clear();
//            }
//        }
//        if (!dataList.isEmpty()) {
//            System.out.println("剩下的：" + dataList);
//        }

        int batchNos = size % limitSize == 0 ? size / limitSize : (size / limitSize) + 1;
        Map<String, List<String>> datas = new HashMap<>();
        int startIndex = 0;
        int stopIndex = 0;
        for (int i = 0; i < batchNos; i++) {
            stopIndex = (i == batchNos - 1) ? size : stopIndex + limitSize;
            List<String> tempList = new ArrayList<>(dataList.subList(startIndex, stopIndex));
            datas.put(String.valueOf(i), tempList);
            startIndex = stopIndex;
        }
        System.out.println(datas);
    }
}
