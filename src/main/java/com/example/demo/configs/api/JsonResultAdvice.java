package com.example.demo.configs.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * 封装统一返回结构
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
@ControllerAdvice
public class JsonResultAdvice implements ResponseBodyAdvice<Object> {
    public JsonResultAdvice() {
        // Do nothing
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public JsonResultDO beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null == body || returnType.getMethod().getReturnType().isAssignableFrom(Void.TYPE)) {
            return JsonResultDO.EMPTY_RESULT;
        }

        if (body instanceof JsonResultDO) {
            return (JsonResultDO) body;
        }

        JsonResultDO result = new JsonResultDO();
        PageData pageData;
        if (body instanceof PageInfo) {
            // PageHelper PageInfo 对象
            PageInfo<?> page = (PageInfo) body;
            pageData = new PageData();
            pageData.setContent(page.getList());
            pageData.setTotal(page.getTotal());
            pageData.setPages(page.getPages());
            pageData.setPageNum(page.getPageNum());
            pageData.setPageSize(page.getPageSize());
            result.setDatas(pageData);
        } else if (body instanceof Page) {
            // PageHelper Page 对象
            Page<?> page = (Page) body;
            pageData = new PageData();
            pageData.setContent(page.getResult());
            pageData.setTotal(page.getTotal());
            pageData.setPages(page.getPages());
            pageData.setPageNum(page.getPageNum());
            pageData.setPageSize(page.getPageSize());
            result.setDatas(pageData);
        } else if (body instanceof org.springframework.data.domain.Page) {
            // Spring data 对象
            org.springframework.data.domain.Page<?> page = (org.springframework.data.domain.Page) body;
            pageData = new PageData();
            pageData.setContent(page.getContent());
            pageData.setTotal(page.getTotalElements());
            pageData.setPages(page.getTotalPages());
            pageData.setPageNum(page.getNumber() + 1);
            pageData.setPageSize(page.getSize());
            result.setDatas(pageData);
        } else if (body instanceof Map) {
            Map map = (Map) body;
            result.setDatas(map);
        } else {
            JSONObject data = new JSONObject();
            data.put("content", body);
            result.setDatas(data);
        }
        return result;
    }
}
