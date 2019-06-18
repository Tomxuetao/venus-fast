package com.venus.modules.job.task;

import com.venus.modules.sys.service.SysCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务：清空验证码
 * sysCaptchaTask为spring bean的名称,清空验证码
 *
 * @author Tomxuetao
 */
@Component("sysCaptchaTask")
public class SysCaptchaTask implements ITask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Override
    public void run(String params) {
        sysCaptchaService.removeAll();
        logger.debug("SysCaptchaTask定时任务正在执行，参数为：{}", params);
        logger.debug("清空验证码成功");
    }
}
