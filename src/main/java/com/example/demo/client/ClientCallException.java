package com.example.demo.client;

import lombok.Data;

/**
 * 客户端调用异常类
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Data
public class ClientCallException extends Exception {
    private static final long serialVersionUID = -3517201997699093661L;

    private String errorCode;

    public ClientCallException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
