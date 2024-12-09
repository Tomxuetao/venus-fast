package com.venus.common.handler;

import cn.hutool.core.map.MapUtil;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.ExceptionUtils;
import com.venus.common.exception.VenusException;
import com.venus.common.utils.HttpContextUtils;
import com.venus.common.utils.IpUtils;
import com.venus.common.utils.JsonUtils;
import com.venus.common.utils.Result;
import com.venus.modules.log.entity.SysLogErrorEntity;
import com.venus.modules.log.service.SysLogErrorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class VenusExceptionHandler {
    private final SysLogErrorService sysLogErrorService;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(VenusException.class)
    public Result handleRenException(VenusException ex) {
        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());

        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        Result result = new Result();
        result.error(ErrorCode.DB_RECORD_EXISTS);

        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        saveLog(ex);

        return new Result().error();
    }

    /**
     * 保存异常日志
     */
    private void saveLog(Exception ex) {
        SysLogErrorEntity log = new SysLogErrorEntity();

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        assert request != null;
        log.setIp(IpUtils.getIpAddr(request));
        log.setIp(IpUtils.getIpAddr(request));
        log.setRequestMethod(request.getMethod());
        log.setRequestUri(request.getRequestURI());
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        Map<String, String> params = HttpContextUtils.getParameterMap(request);
        if (MapUtil.isNotEmpty(params)) {
            log.setRequestParams(JsonUtils.toJsonString(params));
        }

        //异常信息
        log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));

        //保存
        sysLogErrorService.save(log);
    }
}