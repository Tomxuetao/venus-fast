package com.venus.modules.msg.cloud;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dm20151123.AsyncClient;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailRequest;
import com.aliyun.sdk.service.dm20151123.models.SingleSendMailResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AliyunCloudMailService extends AbstractCloudMailService {
    public AliyunCloudMailService(CloudMailConfig config) {
        this.config = config;
    }

    @Override
    public String send(String to, String code, Long templateId) throws ExecutionException, InterruptedException {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(config.getAliyunAccessKeyId())
                .accessKeySecret(config.getAliyunAccessKeySecret())
                .build());
        AsyncClient client = AsyncClient.builder()
                .region(config.getAliyunRegion())
                .credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride(config.getAliyunEndPoint()))
                .build();
        SingleSendMailRequest singleSendMailRequest = SingleSendMailRequest.builder()
                .accountName(config.getAliyunAccountName())
                .fromAlias("VenusAdmin")
                .addressType(1)
                .replyToAddress(false)
                .toAddress(to)
                .subject("电子邮件验证码：" + code)
                .htmlBody(code)
                .build();
        CompletableFuture<SingleSendMailResponse> response = client.singleSendMail(singleSendMailRequest);
        SingleSendMailResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));
        client.close();
        return new Gson().toJson(resp);
    }
}
