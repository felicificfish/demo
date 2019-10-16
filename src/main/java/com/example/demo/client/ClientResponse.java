package com.example.demo.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 调用客户端响应结果类
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class ClientResponse implements Serializable {
    private static final long serialVersionUID = -2023736291321664703L;
    /**
     * 返回码
     */
    private String code;

    /**
     * 调用状态: true/false
     */
    private boolean success = true;

    /**
     * 返回错误信息
     */
    private String msg;
    /**
     * 返回结果集合
     */
    private JSONObject result;

    public static ClientResponse errorResponse(String errorCode, String errorMessage) {
        final ClientResponse response = new ClientResponse();
        response.setSuccess(false);
        response.setCode(errorCode);
        response.setMsg(errorMessage);
        return response;
    }

    public void error(String errorCode, String errorMessage) {
        this.success = false;
        this.code = errorCode;
        this.msg = errorMessage;
    }

    public void error(String errorCode, String errorMessage, JSONObject result) {
        this.success = false;
        this.code = errorCode;
        this.msg = errorMessage;
        this.result = result;
    }

    public void response(boolean success, String code, String msg, JSONObject result) {
        this.setSuccess(success);
        this.setCode(code);
        this.setMsg(msg);
        this.setResult(result);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
