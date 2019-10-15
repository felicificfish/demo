package com.example.demo.service.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * 定时任务demo
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Log4j2
@JobHandler(value = "myDemoJob")
@Component
public class DemoJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>> {}", s);
        return IJobHandler.SUCCESS;
    }
}
