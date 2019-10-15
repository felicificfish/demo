package com.example.demo.service.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.log4j.Log4j2;

/**
 * 定时任务demo
 *
 * @author zhou.xy
 * @date 2019/10/15
 * @since 1.0
 */
@Log4j2
public class DemoGlueJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>>>>>>>>> Hello xxl job!");
        return ReturnT.SUCCESS;
    }
}
