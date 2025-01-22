package com.venus.modules.sys.service.impl;

import com.venus.common.constant.Constant;
import com.venus.common.utils.SpringContextUtils;
import com.venus.common.utils.SseMsg;
import com.venus.modules.sys.service.SysSseService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class SysSseServiceImpl implements SysSseService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EhCacheManager ehCacheManager;
    private Cache<Long, SseEmitter> emitters;

    @Override
    public SseEmitter createEmitter(Long userId) {
        ehCacheManager = ehCacheManager == null ? SpringContextUtils.getBean(EhCacheManager.class) : ehCacheManager;
        emitters = emitters == null ? ehCacheManager.getCache(Constant.SYS_SSE_USER_CACHE) : emitters;
        SseEmitter sseEmitter = new SseEmitter(0L);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                // 每 30 秒发送一个事件，保持连接
                sseEmitter.send(new SseMsg<>(1, "保持心跳"));
            } catch (IOException e) {
                // 如果连接中断，完成 Emitter
                sseEmitter.complete();
            }
        }, 0, 30, TimeUnit.SECONDS);
        sseEmitter.onCompletion(() -> {
            logger.info("SSE连接关闭，userId：{}", userId);
            closeEmitter(userId);
        });
        sseEmitter.onTimeout(() -> {
            logger.info("SSE连接超时，userId：{}", userId);
            closeEmitter(userId);
        });

        emitters.put(userId, sseEmitter);
        logger.info("SSE数量，emitters-count：{}", emitters.size());
        return sseEmitter;
    }

    @Override
    public void closeEmitter(Long userId) {
        if(emitters.get(userId) != null) {
            emitters.get(userId).complete();
            emitters.remove(userId);
            logger.info("SSE关闭成功，userId：{}", userId);
        }
    }

    @Override
    public void sendMsg(Long userId, SseMsg msg) {
        if(emitters.get(userId) != null) {
            try {
                emitters.get(userId).send(msg);
                logger.info("SSE连接发送消息，msg：{}", msg.getBody());
            } catch (Exception e) {
                logger.error("SSE连接发送消息失败，userId：{}", userId, e);
            }
        }
    }
}
