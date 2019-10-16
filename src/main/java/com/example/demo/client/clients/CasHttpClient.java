package com.example.demo.client.clients;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.example.demo.client.ClientCallException;
import com.example.demo.client.ClientResponse;
import com.example.demo.client.ExternalInterfaceAPI;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Objects;

/**
 * 权限系统服务
 *
 * @author zhou.xy
 * @date 2019/10/14
 * @since 1.0
 */
@Component
public class CasHttpClient extends DefaultHttpClient implements InitializingBean {
    @Value("${third.party.service.address.cas}")
    private String casUrl;
    private String name = CasAPI.class.getSimpleName();

    @Override
    public String getURL(ExternalInterfaceAPI externalInterfaceAPI, Map<String, Object> params) {
        return casUrl + externalInterfaceAPI.getExternalServiceInterface();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> forAPI() {
        return CasAPI.class;
    }

    @Override
    public ClientResponse call(ExternalInterfaceAPI externalInterfaceAPI, Map<String, Object> params)
            throws ClientCallException, IOException {
        switch (externalInterfaceAPI.getMethod()) {
            case GET:
                if (CasAPI.MENU.getServiceId().equals(externalInterfaceAPI.getServiceId())) {
                    return afterClientResponseForLargeData(get(getURL(externalInterfaceAPI, params), params));
                } else {
                    return afterClientResponse(get(getURL(externalInterfaceAPI, params), params));
                }
            case PUT:
                return afterClientResponse(put(getURL(externalInterfaceAPI, params), params));
            default: {
                String message = "请求方式不支持,method=" + externalInterfaceAPI.getMethod();
                return ClientResponse.errorResponse("request.method.not.support", message);
            }
        }
    }

    /**
     * 封装返回结果
     *
     * @param resultStr
     * @return ClientResponse
     * @author zhou.xy
     * @date 2019/10/15
     * @since 1.0
     */
    private ClientResponse afterClientResponse(String resultStr) {
        final ClientResponse response = new ClientResponse();
        if (!StringUtils.isEmpty(resultStr)) {
            try {
                final JSONObject json = JSONObject.parseObject(resultStr);
                if (json.getBoolean("success")) {
                    response.setResult(json.getJSONObject("datas"));
                } else {
                    response.error(json.getString("msgCode"), json.getString("i18nmessage"),
                            json.getJSONObject("datas"));
                }
            } catch (JSONException jsone) {
                response.error("incorrect.parameter.analysis", "响应参数解析有误,请联系管理员");
            }
        } else {
            response.error("response.empty", "业务系统无返回或返回为空");
        }
        return response;
    }

    /**
     * 针对大数据量结果封装
     *
     * @param resultStr
     * @return ClientResponse
     * @author zhou.xy
     * @date 2019/10/15
     * @since 1.0
     */
    private ClientResponse afterClientResponseForLargeData(String resultStr) {
        final ClientResponse response = new ClientResponse();
        if (!StringUtils.isEmpty(resultStr)) {
            JSONReader reader = null;
            try {
                reader = new JSONReader(new StringReader(resultStr));
            } catch (Exception e) {
                response.error("incorrect.parameter.analysis", "响应参数解析有误,请联系管理员");
            }
            reader.startObject();
            String i18nMessage = null;
            Boolean success = null;
            JSONObject datas = new JSONObject();
            String msgCode = null;
            while (reader.hasNext()) {
                String key = reader.readString();
                if ("success".equals(key)) {
                    success = (Boolean) reader.readObject();
                } else if ("i18nmessage".equals(key)) {
                    i18nMessage = Objects.toString(reader.readObject());
                } else if ("datas".equals(key)) {
                    reader.startObject();
                    while (reader.hasNext()) {
                        String arrayListItemKey = reader.readString();
                        Object arrayListItemValue = reader.readObject();
                        datas.put(arrayListItemKey, arrayListItemValue);
                    }
                    reader.endObject();
                } else if ("msgCode".equals(key)) {
                    msgCode = Objects.toString(reader.readObject());
                } else {
                    reader.readObject();
                }
            }
            reader.endObject();
            if (success) {
                response.setResult(datas);
            } else {
                response.error(msgCode, i18nMessage, datas);
            }
        } else {
            response.error("response.empty", "业务系统无返回或返回为空");
        }
        return response;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
