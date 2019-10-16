package com.example.demo.client.clients;

import com.example.demo.client.ExternalInterfaceAPI;
import com.example.demo.client.HttpMethods;

/**
 * 权限系统API
 *
 * @author zhou.xy
 * @date 2019/10/14
 * @since 1.0
 */
public enum CasAPI implements ExternalInterfaceAPI {
    LOGIN("cas.login", "/login", HttpMethods.GET),
    CHECK_MOBILE("cas.check.mobile", "/plc/check/mobile", HttpMethods.GET),
    MENU("cas.menu", "/menutree", HttpMethods.GET),
    UPDATE_PASSWORD("cas.update.password", "/user/psw", HttpMethods.PUT);

    private String serviceId;
    private String externalServiceInterface;
    private HttpMethods method;
    CasAPI(String serviceId, String externalServiceInterface, HttpMethods method) {
        this.serviceId = serviceId;
        this.externalServiceInterface = externalServiceInterface;
        this.method = method;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getExternalServiceInterface() {
        return externalServiceInterface;
    }

    @Override
    public HttpMethods getMethod() {
        return method;
    }
}
