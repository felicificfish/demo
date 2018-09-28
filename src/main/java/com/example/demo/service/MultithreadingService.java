package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Service
public class MultithreadingService {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);

    public String multithreading(List<String> names) {
        THREAD_POOL.execute(new HandlerProcess(names));
        return "true";
    }

    private String process(String name) {
        log.info(name);
        return name;
    }

    private class HandlerProcess implements Runnable, Serializable {
        private List<String> names;

        HandlerProcess(List<String> names) {
            this.names = names;
        }

        @Override
        public void run() {
            try {
                for (String name : names) {
                    process(name);
                }
            } catch (Exception e) {
                log.error("出现异常:" + JSON.toJSONString(this), e);
            }
        }
    }
}
