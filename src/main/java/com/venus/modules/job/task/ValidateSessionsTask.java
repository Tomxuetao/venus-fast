package com.venus.modules.job.task;

import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("validateSessionsTask")
public class ValidateSessionsTask implements ITask {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("sessionManager")
    @Lazy
    private ValidatingSessionManager sessionManager;

    @Override
    public void run(String params) {
        logger.info("ValidateSessionsTask定时任务，参数：{}", params);

        sessionManager.validateSessions();
    }
}
