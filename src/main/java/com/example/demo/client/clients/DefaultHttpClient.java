package com.example.demo.client.clients;

import com.alibaba.fastjson.JSON;
import com.example.demo.client.Client;
import com.example.demo.client.ClientCallException;
import com.example.demo.client.ExternalInterfaceAPI;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 客户端通用类
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Log4j2
public abstract class DefaultHttpClient implements Client {
    protected static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
    private static int READ_TIMEOUT = 60000;
    private static int CONNECT_TIMEOUT = 60000;
    private static int CONNECTION_POOL_MAX_IDEL = 5;
    private static int CONNECTION_POOL_KEEP_ALIVE = 5;
    protected OkHttpClient client;

    public DefaultHttpClient() {
        final ConnectionPool connectionPool = new ConnectionPool(CONNECTION_POOL_MAX_IDEL,
                CONNECTION_POOL_KEEP_ALIVE, TimeUnit.SECONDS);
        this.client = new OkHttpClient()
                .newBuilder()
                .connectionPool(connectionPool)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public String get(String url, Map<String, Object> params) throws IOException, ClientCallException {
        List<String> paramList = new ArrayList<>();
        Request.Builder builder = new Request.Builder();
        url = paramsAppendToUrl(url, params, paramList, builder);
        Request request = builder.url(url).build();
        return execute(request);
    }

    public String put(String url, Map<String, Object> params) throws IOException, ClientCallException {
        List<String> paramList = new ArrayList<>();
        Request.Builder builder = new Request.Builder();
        url = paramsAppendToUrl(url, params, paramList, builder);
        RequestBody requestBody = RequestBody.create(TYPE_JSON, JSON.toJSONString(params));
        Request request = builder.url(url).put(requestBody).build();
        return execute(request);
    }

    private String paramsAppendToUrl(String url, Map<String, Object> params, List<String> paramList, Request.Builder builder) {
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                paramList.add(entry.getKey() + "=" + entry.getValue());
                if (Objects.equals(entry.getKey(), "Authorization")) {
                    builder.addHeader("Authorization", Objects.toString(entry.getValue()));
                }
            }
            url += "?" + StringUtils.join(paramList, "&");
        }
        return url;
    }

    private String execute(Request request) throws ClientCallException, IOException {
        final Call call = client.newCall(request);
        try {
            final Response response = call.execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new ClientCallException("999999", response.message());
            }
        } finally {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }

    public abstract String getURL(ExternalInterfaceAPI externalInterfaceAPI, Map<String, Object> params);
}
