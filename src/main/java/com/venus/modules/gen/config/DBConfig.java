package com.venus.modules.gen.config;

import com.venus.common.exception.VenusException;
import com.venus.modules.gen.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class DBConfig {
    @Value("${venus.database: postgresql}")
    private String database;
    @Resource
    private MySQLGenDao mySQLGenDao;
    @Resource
    private OracleGenDao oracleGenDao;
    @Resource
    private SQLServerGenDao sqlServerGenDao;
    @Resource
    private PostgreGenDao postgreGenDao;
    @Resource
    private DmGenDao dmGeneratorDao;

    @Bean
    @Primary
    public SysGenDao getGeneratorDao() {
        if ("mysql".equalsIgnoreCase(database)) {
            return mySQLGenDao;
        } else if ("oracle".equalsIgnoreCase(database)) {
            return oracleGenDao;
        } else if ("sqlserver".equalsIgnoreCase(database)) {
            return sqlServerGenDao;
        } else if ("postgresql".equalsIgnoreCase(database)) {
            return postgreGenDao;
        } else if ("dm".equalsIgnoreCase(database)) {
            return dmGeneratorDao;
        } else {
            throw new VenusException("不支持当前数据库：" + database);
        }
    }
}
