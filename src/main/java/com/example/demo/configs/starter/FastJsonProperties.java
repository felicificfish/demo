package com.example.demo.configs.starter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("spring.http.converters.fastjson")
public class FastJsonProperties {
    private List<SerializerFeature> features;

    public FastJsonProperties() {

    }

}
