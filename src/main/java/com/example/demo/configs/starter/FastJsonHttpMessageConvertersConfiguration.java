package com.example.demo.configs.starter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({FastJsonProperties.class})
@ConditionalOnClass(
        name = {"com.alibaba.fastjson.JSON"}
)
@ConditionalOnProperty(
        name = {"spring.http.converters.preferred-json-mapper"},
        havingValue = "fastjson"
)
public class FastJsonHttpMessageConvertersConfiguration {
    private static Charset UTF_8 = Charset.forName("UTF-8");

    @Autowired
    private FastJsonProperties properties;

    public FastJsonHttpMessageConvertersConfiguration() {

    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        fastConvert.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", UTF_8), new MediaType("application", "*+json", UTF_8), new MediaType("application", "jsonp", UTF_8), new MediaType("application", "*+jsonp", UTF_8), new MediaType("text", "html", UTF_8)));
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(new SerializerFeature[]{SerializerFeature.PrettyFormat});
        List<SerializerFeature> features = this.properties.getFeatures();
        if (!CollectionUtils.isEmpty(features)) {
            SerializerFeature[] featureArray = new SerializerFeature[features.size()];
            fastJsonConfig.setSerializerFeatures((SerializerFeature[]) features.toArray(featureArray));
        }

        fastConvert.setFastJsonConfig(fastJsonConfig);
        log.info("Initialized fastJsonHttpMessageConverters");
        return new HttpMessageConverters(new HttpMessageConverter[]{fastConvert});
    }
}


