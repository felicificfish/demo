package com.example.demo.client;

/**
 * 服务枚举接口类，所有外部服务枚举类均需要实现此接口
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
public interface ExternalInterfaceAPI {
    String getServiceId();

    String getExternalServiceInterface();

    HttpMethods getMethod();
}
