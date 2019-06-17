package com.example.demo.constant;

/**
 * 钉钉网关地址
 *
 * @author zhou.xy
 * @since 2019/6/14
 */
public class DingTalkUrl {
    /**
     * 获取access_token
     */
    public static final String GET_TOKEN = "https://oapi.dingtalk.com/gettoken";
    /**
     * 获取部门用户详情
     */
    public static final String GET_USER_LIST_DETAIL = "https://oapi.dingtalk.com/user/listbypage";
    /**
     * 获取子部门ID列表
     */
    public static final String GET_DEPARTMENT_ID_LIST = "https://oapi.dingtalk.com/department/list_ids";
    /**
     * 获取部门详情
     */
    public static final String GET_DEPARTMENT_DETAIL = "https://oapi.dingtalk.com/department/get";
    /**
     * 获取角色列表
     */
    public static final String GET_ROLE_LIST = "https://oapi.dingtalk.com/topapi/role/list";
    /**
     * 发送工作通知消息
     */
    public static final String ASYNCSEND_V2 = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
}
