package com.example.demo.configs.api;

import com.alibaba.fastjson.JSON;
import com.example.demo.utils.SpringContext;
import lombok.Data;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;

/**
 * 接口统一返回结构
 */
@Data
public class JSONResultDO implements Serializable {
    public static final JSONResultDO EMPTY_RESULT = new JSONResultDO();
    private boolean success = true;
    private String msgCode;
    private Object[] arguments;
    private Object datas;

    public JSONResultDO() {
    }

    public JSONResultDO(String msgcode) {
        this.success = false;
        this.msgCode = msgcode;
    }

    public JSONResultDO(boolean success, String msgcode) {
        this.success = success;
        this.msgCode = msgcode;
    }

    public JSONResultDO(boolean success, String msgCode, Object[] arguments) {
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
