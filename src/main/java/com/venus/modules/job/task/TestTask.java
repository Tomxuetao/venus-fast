package com.venus.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("testTask")
public class TestTask implements ITask{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(String params){
        logger.debug("TestTask定时任务正在执行，参数为：{}", params);
    }
}
