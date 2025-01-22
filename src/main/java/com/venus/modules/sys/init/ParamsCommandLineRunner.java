package com.venus.modules.sys.init;

import com.venus.modules.sys.dto.SysParamsDTO;
import com.venus.modules.sys.redis.SysParamsRedis;
import com.venus.modules.sys.service.SysParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ParamsCommandLineRunner implements CommandLineRunner {

    @Autowired
    SysParamsRedis sysParamsRedis;

    @Autowired
    SysParamsService sysParamsService;

    @Override
    public void run(String... args) {
        List<SysParamsDTO> paramsList = sysParamsService.list(new HashMap<>());

        for (SysParamsDTO params : paramsList) {
            sysParamsRedis.set(params.getParamCode(), params.getParamValue());
        }
    }
}
