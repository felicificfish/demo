package com.example.demo.client;

import java.io.IOException;
import java.util.Map;

/**
 * 客户端接口类
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
public interface Client {
    String getName();

    Class<?> forAPI();

    ClientResponse call(ExternalInterfaceAPI externalInterfaceAPI, Map<String, Object> params) throws ClientCallException, IOException;
}
