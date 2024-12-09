package com.venus.modules.msg.cloud;

import java.util.concurrent.ExecutionException;

public abstract class AbstractCloudMailService {
    CloudMailConfig config;

    public abstract String send(String to, String code, Long templateId) throws ExecutionException, InterruptedException;
}
