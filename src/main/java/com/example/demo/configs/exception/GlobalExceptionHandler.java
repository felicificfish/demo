package com.example.demo.configs.exception;

import com.example.demo.configs.api.JSONResultDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({SweetException.class})
    @ResponseBody
    public JSONResultDO jsonErrorHandler(HttpServletRequest req, SweetException e) {
        log.error("Catch a exception in API[{}]", req.getRequestURL().toString(), e);
        JSONResultDO resp = new JSONResultDO(false, e.getExCode(), e.getArguments());
        return resp;
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public JSONResultDO jsonErrorHandler(HttpServletRequest req, Exception e) {
        if (e instanceof ValidateException) {
            return new JSONResultDO(e.getMessage());
        } else {
            log.error("Catch a exception in API[{}]", req.getRequestURL().toString(), e);
            return new JSONResultDO("5500");
        }
    }
}
