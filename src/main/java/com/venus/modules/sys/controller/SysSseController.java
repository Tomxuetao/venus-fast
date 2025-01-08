package com.venus.modules.sys.controller;

import com.venus.modules.login.entity.SysUserTokenEntity;
import com.venus.modules.login.service.ShiroService;
import com.venus.modules.sys.service.SysSseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sys/sse")
public class SysSseController {

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private SysSseService sysSseService;

    @GetMapping("create")
    @ApiOperation("创建SSE")
    public SseEmitter create(HttpServletResponse response, String token) {
        response.setContentType("text/event-stream");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", "no-cache");
        SysUserTokenEntity userToken = shiroService.getByToken(token);
        return sysSseService.createEmitter(userToken.getUserId());
    }
}
