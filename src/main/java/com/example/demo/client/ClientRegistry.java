package com.example.demo.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端注册容器类
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Component
public class ClientRegistry implements InitializingBean {
    @Autowired(required = false)
    private Client[] clients;

    private Map<String, Client> clientMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        clientMap = new HashMap<>();
        if (null != clients) {
            for (Client client : clients) {
                clientMap.put(client.forAPI().getSimpleName(), client);
            }
        }
    }

    public Client getClient(ExternalInterfaceAPI externalInterfaceAPI) {
        return clientMap.get(externalInterfaceAPI.getClass().getSimpleName());
    }
}
