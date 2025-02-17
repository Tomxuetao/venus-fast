package com.venus.modules.oss.cloud;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.ClientBuilderConfiguration;

import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AliyunCloudStorageService extends AbstractCloudStorageService {
    public AliyunCloudStorageService(CloudStorageConfig config) {
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        OSS client = new OSSClientBuilder().build(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret(), conf);
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
            client.shutdown();
        } catch (Exception e) {
            throw new VenusException(ErrorCode.OSS_UPLOAD_FILE_ERROR, e, "");
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public InputStream readStream(String path) {
        return null;
    }

    @Override
    public boolean exists(String url) {
        return false;
    }
}
