package com.venus.modules.service.impl;

import com.venus.modules.entity.MsgMail;
import com.venus.modules.mapper.MsgMailMapper;
import com.venus.modules.service.IMsgMailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮件发送日志 服务实现类
 * </p>
 *
 * @author Tomxuetao
 * @since 2024-11-21
 */
@Service
public class MsgMailServiceImpl extends ServiceImpl<MsgMailMapper, MsgMail> implements IMsgMailService {

}
