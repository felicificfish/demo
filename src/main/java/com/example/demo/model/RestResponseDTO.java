package com.example.demo.model;

import lombok.Data;

/**
 * TODO
 *
 * @author zhou.xy
 * @date 2019/8/8
 * @since 1.0
 */
@Data
public class RestResponseDTO<T> {
    public static final Integer UNKNOWN_ERROR = -1;

    T data;
    String message;
    int statusCode;
}
