package com.venus.modules.sys.service;

import com.venus.common.utils.SseMsg;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SysSseService {

    SseEmitter createEmitter(Long userId);

    void closeEmitter(Long userId);

    void sendMsg(Long userId, SseMsg msg);
}
