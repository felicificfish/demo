package com.example.demo.configs.api;

import com.alibaba.fastjson.JSON;
import com.example.demo.utils.SpringContext;
import lombok.Data;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;

/**
 * 返回结果
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
public class JsonResultDO implements Serializable {
    public static final JsonResultDO EMPTY_RESULT = new JsonResultDO();
    private boolean success = true;
    private String msgCode;
    private Object[] arguments;
    private Object datas;

    public JsonResultDO() {
    }

    public JsonResultDO(String msgcode) {
        this.success = false;
        this.msgCode = msgcode;
    }

    public JsonResultDO(boolean success, String msgCode) {
        this.success = success;
        this.msgCode = msgCode;
    }

    public JsonResultDO(boolean success, String msgCode, Object[] arguments) {
        this.success = success;
        this.msgCode = msgCode;
        this.arguments = arguments;
    }

    public String getI18nMessage() {
        Locale locale = Locale.CHINA;
        return ((MessageSource) SpringContext.getBean(MessageSource.class)).getMessage(this.msgCode, this.arguments, this.msgCode, locale);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
