package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @author zhou.xy
 * @date 2019/8/8
 * @since 1.0
 */
@AllArgsConstructor
public class FilterRestTemplate implements RestOperations {
    @Delegate
    protected volatile RestTemplate restTemplate;
}
