package com.venus.modules.msg.cloud;

import com.venus.common.constant.Constant;
import com.venus.common.utils.SpringContextUtils;
import com.venus.modules.sys.service.SysParamsService;

public final class MailMsgFactory {
    private static SysParamsService sysParamsService;

    static {
        MailMsgFactory.sysParamsService = SpringContextUtils.getBean(SysParamsService.class);
    }

    public static AbstractCloudMailService build() {
        CloudMailConfig config = sysParamsService.getValueObject(Constant.CLOUD_MAIL_CONFIG_KEY, CloudMailConfig.class);
        int type = config.getType();
        if (type == Constant.MsgCloudService.ALIYUN.getValue()) {
            return new AliyunCloudMailService(config);
        }
        return null;
    }
}
