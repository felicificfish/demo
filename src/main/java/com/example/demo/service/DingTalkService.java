package com.example.demo.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.example.demo.constant.DingTalkUrl;
import com.example.demo.model.DingTalkUserDO;
import com.taobao.api.ApiException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 钉钉服务
 *
 * @author zhou.xy
 * @since 2019/6/14
 */
@Log4j2
@Service
public class DingTalkService {
    /**
     * 应用的AppKey
     */
    @Value("${ding.talk.appKey}")
    public String appKey;
    /**
     * 应用的AppSecret
     */
    @Value("${ding.talk.appSecret}")
    public String appSecret;
    /**
     * 应用的agentdId
     */
    @Value("${ding.talk.agentId}")
    public Long agentId;

    /**
     * 获取accessToken
     *
     * @return
     */
    public String getToken() {
        DefaultDingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.GET_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            if (null != response && response.getErrcode() == 0) {
                // 正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
                return response.getAccessToken();
            } else {
                log.error("Ding talk get access token fail, response={}", response);
            }
        } catch (ApiException e) {
            log.error("Ding talk get access token error", e);
        }
        return "";
    }

    /**
     * @param departmentId 必填	获取的部门id，1表示根部门
     * @param offset       必填 支持分页查询，与size参数同时设置时才生效，此参数代表偏移量,偏移量从0开始
     * @param size         必填	支持分页查询，与offset参数同时设置时才生效，此参数代表分页大小，最大100
     * @param order        非必填  支持分页查询，部门成员的排序规则，默认 是按自定义排序；
     *                     entry_asc：代表按照进入部门的时间升序，
     *                     entry_desc：代表按照进入部门的时间降序，
     *                     modify_asc：代表按照部门信息修改时间升序，
     *                     modify_desc：代表按照部门信息修改时间降序，
     *                     custom：代表用户定义(未定义时按照拼音)排序
     * @return [{
     * "active":true,"avatar":"","department":"[1]","isAdmin":false,"isBoss":false,
     * "isHide":false,"isLeader":false,"jobnumber":"","mobile":"18610313708",
     * "name":"屠锋锋","order":176392210679274500,
     * "position":"","unionid":"NiSdcOJxeoGH74WfbAfyTLAiEiE","userid":"045650276923946688"
     * }]
     */
    public List<OapiUserListbypageResponse.Userlist> getUserDetailByDepartment(Long departmentId, Long offset, Long size, String order) {
        if (null == departmentId) {
            departmentId = 1L;
        }
        if (null == offset) {
            offset = 0L;
        }
        if (null == size) {
            size = 100L;
        }
        DingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.GET_USER_LIST_DETAIL);
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        request.setDepartmentId(departmentId);
        request.setOffset(offset);
        request.setSize(size);
        if (StringUtils.isEmpty(order)) {
            request.setOrder("entry_desc");
        } else {
            request.setOrder(order);
        }
        request.setHttpMethod("GET");
        try {
            OapiUserListbypageResponse response = client.execute(request, getToken());
            if (null != response && response.getErrcode() == 0) {
                return response.getUserlist();
            }
        } catch (ApiException e) {
            log.error("Ding talk get user listbypage error", e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 获取子部门ID列表
     *
     * @param id 必填 父部门id。根部门的话传1
     * @return [968252, 971020, 42432174, 61205147]
     */
    public List<Long> getDepartmentIdList(String id) {
        if (StringUtils.isEmpty(id)) {
            id = "1";
        }
        DingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.GET_DEPARTMENT_ID_LIST);
        OapiDepartmentListIdsRequest request = new OapiDepartmentListIdsRequest();
        request.setId(id);
        request.setHttpMethod("GET");
        try {
            OapiDepartmentListIdsResponse response = client.execute(request, getToken());
            if (null != response && response.getErrcode() == 0) {
                return response.getSubDeptIdList();
            }
        } catch (ApiException e) {
            log.error("Ding talk get department id list error", e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 获取部门详情
     *
     * @param id 必填  	部门id
     * @return
     */
    public OapiDepartmentGetResponse getDepartmentDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            id = "1";
        }
        DingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.GET_DEPARTMENT_DETAIL);
        OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
        request.setId(id);
        request.setHttpMethod("GET");
        try {
            OapiDepartmentGetResponse response = client.execute(request, getToken());
            if (null != response && response.getErrcode() == 0) {
                return response;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取角色列表
     *
     * @param offset Number	非必填	分页偏移，默认值：0
     * @param size   Number	非必填	分页大小，默认值：20，最大值200
     * @return [{"groupId":24652804,"name":"默认","roles":[{"id":24652808,"name":"主管"}]}]
     */
    public List<OapiRoleListResponse.OpenRoleGroup> getRoleList(Long offset, Long size) {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.GET_ROLE_LIST);
        OapiRoleListRequest request = new OapiRoleListRequest();
        if (null != offset) {
            request.setOffset(offset);
        }
        if (null != size) {
            request.setSize(size);
        }
        try {
            OapiRoleListResponse response = client.execute(request, getToken());
            if (null != response && response.getErrcode() == 0) {
                return response.getResult().getList();
            }
        } catch (ApiException e) {
            log.error("Ding talk get role list error", e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 发送工作通知消息
     *
     * @param content
     * @param userIdList 可选	接收者的用户userid列表，最大列表长度：100，多个以英文半角逗号隔开，如：zhangsan,lisi
     */
    public void sendWorkMsg(String content, String userIdList) {
        if (StringUtils.isEmpty(userIdList)) {
            return;
        }
        //发送消息
        DingTalkClient client = new DefaultDingTalkClient(DingTalkUrl.ASYNCSEND_V2);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIdList);
        request.setAgentId(agentId);
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

    /**
     * 获取部门信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> getDepartmentInfo(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("departmentListIds", getDepartmentIdList(id));
        result.put("departmentDetail", getDepartmentDetail(id));
        return result;
    }

    /**
     * 获取部门用户，缓存有效时长24小时
     *
     * @param departmentIds 部门ID集合，多个以英文半角逗号隔开
     * @return
     */
    public List<DingTalkUserDO> getUserInfo(String departmentIds) {
        if (StringUtils.isEmpty(departmentIds)) {
            return Collections.EMPTY_LIST;
        }
        List<DingTalkUserDO> result = new ArrayList<>();
        String[] split = departmentIds.split(",");
        List<Long> idList = new ArrayList<>();
        for (String id : split) {
            idList.add(Long.valueOf(id));
        }
        for (Long id : idList) {
            List<OapiUserListbypageResponse.Userlist> userList = getUserDetailByDepartment(id, 0L, 100L, null);
            if (!CollectionUtils.isEmpty(userList)) {
                for (OapiUserListbypageResponse.Userlist user : userList) {
                    DingTalkUserDO dingTalkUserDO = new DingTalkUserDO();
                    BeanUtils.copyProperties(user, dingTalkUserDO);
                    result.add(dingTalkUserDO);
                }
            }
        }
        return result;
    }
}
