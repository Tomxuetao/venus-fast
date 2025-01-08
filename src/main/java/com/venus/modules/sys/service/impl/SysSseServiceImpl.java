package com.venus.modules.sys.service.impl;

import com.venus.common.utils.SseMsg;
import com.venus.modules.sys.service.SysSseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SysSseServiceImpl implements SysSseService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createEmitter(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> {
            logger.info("SSE连接关闭，userId：{}", userId);
            closeEmitter(userId);
        });
        sseEmitter.onTimeout(() -> {
            logger.info("SSE连接超时，userId：{}", userId);
            closeEmitter(userId);
        });
        try {
            SseMsg<String> msg = new SseMsg<>(1, "连接成功");
            sseEmitter.send(msg);
        } catch (Exception e) {
            logger.error("SSE连接发送消息失败，userId：{}", userId, e);
        }
        emitters.put(userId, sseEmitter);
        System.out.println(emitters.size());
        logger.info("SSE连接创建成功，userId：{}", userId);
        return sseEmitter;
    }

    @Override
    public void closeEmitter(Long userId) {
        if(emitters.containsKey(userId)) {
            emitters.get(userId).complete();
            emitters.remove(userId);
        }
    }

    @Override
    public void sendMsg(Long userId, SseMsg msg) {
        if(emitters.containsKey(userId)) {
            try {
                emitters.get(userId).send(msg);
            } catch (Exception e) {
                logger.error("SSE连接发送消息失败，userId：{}", userId, e);
            }
        }
    }
}
