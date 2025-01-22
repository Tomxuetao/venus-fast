package com.venus.modules.geo.config;

import lombok.Data;

import java.io.Serializable;

/**
 * GeoServer配置
 */
@Data
public class GeoServerConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serverUrl;

    private String accessKey;

    private String secretKey;

    private String datastore;

    private String workspace;

    private String layerName;
}
