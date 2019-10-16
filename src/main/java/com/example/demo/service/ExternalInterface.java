package com.example.demo.service;

import com.alibaba.fastjson.JSONException;
import com.example.demo.client.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * 第三方接口服务
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Log4j2
@Component
public class ExternalInterface {
    @Autowired
    private ClientRegistry clientRegistry;

    public ClientResponse call(ExternalInterfaceAPI api, Map<String, Object> params) {
        final Client client = clientRegistry.getClient(api);
        if (client == null) {
            return ClientResponse.errorResponse("999999", "不支持的客户端请求");
        }
        try {
            return client.call(api, params);
        } catch (JSONException jsone) {
            return ClientResponse.errorResponse("888888", "JSON解析错误");
        } catch (SocketTimeoutException ste) {
            log.error("请求服务接口接口:{}读取响应超时异常:{}", ste, api.getExternalServiceInterface(), ste.getMessage());
            return ClientResponse.errorResponse("777777", "读取响应超时");
        } catch (ClientCallException cce) {
            return ClientResponse.errorResponse(cce.getErrorCode(), cce.getMessage());
        } catch (Exception e) {
            log.error("请求服务接口接口:{}调用过程中出现未知异常:{}", e, api.getExternalServiceInterface(), e.getMessage());
            return ClientResponse.errorResponse("666666", "调用过程中出现异常");
        }
    }
}
