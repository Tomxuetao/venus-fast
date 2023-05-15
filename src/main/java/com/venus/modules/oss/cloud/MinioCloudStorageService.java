package com.venus.modules.oss.cloud;

import io.minio.*;
import io.minio.errors.MinioException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioCloudStorageService extends AbstractCloudStorageService {

    public MinioCloudStorageService(CloudStorageConfig config) {
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getMinioPrefix(), suffix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            MinioClient minioClient = MinioClient
                    .builder()
                    .endpoint(config.getMinioEndPoint())
                    .credentials(config.getMinioAccessKey(), config.getMinioSecretKey())
                    .build();
            boolean hasBucket = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(config.getMinioBucketName())
                            .build());
            if (!hasBucket) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(config.getMinioBucketName()).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(config.getMinioBucketName())
                            .object(path)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
            throw new RuntimeException(e);
        }
        return config.getMinioBucketName() + "/" + path;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getMinioPrefix(), suffix));
    }
}
