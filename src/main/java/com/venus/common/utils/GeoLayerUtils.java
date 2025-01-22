package com.venus.common.utils;

import com.venus.common.exception.VenusException;
import com.venus.modules.geo.config.GeoServerConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class GeoLayerUtils {
    private static final Logger logger = LoggerFactory.getLogger(GeoLayerUtils.class);

    /**
     * 发布图层
     * @param config Geoserver 配置
     * @param inputStream 输入流
     */
    public static void publishLayer(GeoServerConfig config, InputStream inputStream) {
        // 1. 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 2. 创建 RequestBody，使用 InputStream 作为二进制数据源
        byte[] bytes = ZipFileUtils.changeZipShpStream(inputStream, config.getLayerName());

        // 3. 创建 RequestBody，使用 InputStream 作为二进制数据源
        RequestBody requestBody = RequestBody.create(bytes, MediaType.parse("application/zip"));

        // 4. 构建 PUT 请求
        String url = config.getServerUrl() + "/rest/workspaces/" + config.getWorkspace() + "/datastores/" + config.getDatastore() + "/file.shp";
        Request request = new Request.Builder().url(url).put(requestBody)  // 使用 PUT 方法
                .addHeader("Authorization", Credentials.basic(config.getAccessKey(), config.getSecretKey())).build();

        // 5. 发送请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                assert response.body() != null;
                logger.info("Layer published successfully: {}", response.body().string());
            } else {
                throw new VenusException("发布失败: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            throw new VenusException("发布失败", e);
        }
    }

    /**
     * 获取图层详情
     * @param config Geoserver 配置
     * @return String
     */
    public static String getLayerDetail(GeoServerConfig config) {
        // 1. 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();
        String url = config.getServerUrl() + "/rest/workspaces/" + config.getWorkspace() + "/datastores/" + config.getDatastore() + "/featuretypes/" + config.getLayerName() + ".json";
        logger.info("Get layer detail url: {}", url);
        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", Credentials.basic(config.getAccessKey(), config.getSecretKey())).build();
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            } else {
                throw new VenusException("获取图层详情失败: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            throw new VenusException("获取图层详情失败", e);
        }
    }
}
