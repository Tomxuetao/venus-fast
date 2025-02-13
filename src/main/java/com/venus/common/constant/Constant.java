package com.venus.common.constant;

import lombok.Getter;

public interface Constant {
    /**
     * 成功
     */
    int SUCCESS = 1;
    /**
     * 失败
     */
    int FAIL = 0;
    /**
     * 菜单根节点标识
     */
    Long MENU_ROOT = 0L;
    /**
     * 部门根节点标识
     */
    Long DEPT_ROOT = 0L;
    /**
     * 升序
     */
    String ASC = "asc";
    /**
     * 降序
     */
    String DESC = "desc";
    /**
     * 创建时间字段名
     */
    String CREATE_DATE = "create_date";

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";
    /**
     * 当前页码
     */
    String PAGE = "pageNum";
    /**
     * 每页显示记录数
     */
    String LIMIT = "pageSize";
    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";
    /**
     * 排序方式
     */
    String ORDER = "order";
    /**
     * token header
     */
    String TOKEN_HEADER = "token";

    String Authorization_HEADER = "Authorization";

    /**
     * 在线用户session
     */
    String ONLINE_SESSION = "online_session";

    /**
     * 系统SSE用户缓存
     */
    String SYS_SSE_USER_CACHE = "sys_sse_user_cache";

    /**
     * GeoServer 配置KEY
     */
    String GEOSERVER_CONFIG_KEY = "GEOSERVER_CONFIG_KEY";

    /**
     * 系统在线用户缓存
     */
    String SYS_ONLINE_USER_CACHE = "sys_online_user_cache";

    /**
     * session过期时间
     */
    Long DEFAULT_SESSION_TIMEOUT = 30 * 60 * 1000L;
    /**
     * 最后同步数据库时间
     */
    String LAST_SYNC_DB_TIMESTAMP = "LAST_SYNC_DB_TIMESTAMP";

    /**
     * 邮件配置KEY
     */
    String CLOUD_MAIL_CONFIG_KEY = "CLOUD_MAIL_CONFIG_KEY";

    /**
     * 云存储配置KEY
     */
    String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";

    /**
     * 云存储数据源 说明
     */
    @Getter
    enum OssSource {
        /**
         * 默认的
         */
        DEFAULT(0),
        /**
         * 地理数据 插入到 geo_data 表
         */
        GEODATA(1),
        /**
         * GeoServer 上传到Geoserver 服务器
         */
        GEOSERVER(3),

        /**
         * 其他
         */
        OTHERS(4);

        private final int value;

        OssSource(int value) {
            this.value = value;
        }

    }

    @Getter
    enum OnlineStatus {
        /**
         * 离线
         */
        OFFLINE(0),
        /**
         * 在线
         */
        ONLINE(1);

        private final int value;

        OnlineStatus(int value) {
            this.value = value;
        }
    }
    /**
     * 定时任务状态
     */
    @Getter
    enum ScheduleStatus {
        /**
         * 暂停
         */
        PAUSE(0),
        /**
         * 正常
         */
        NORMAL(1);

        private final int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
    }

    /**
     * 用户状态
     */
    @Getter
    enum UserStatus {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 禁用
         */
        DISABLE(0);

        private final int value;

        UserStatus(int value) {
            this.value = value;
        }
    }

    /**
     * 云服务商
     */
    @Getter
    enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3),

        /**
         * Minio
         */
        MINIO(4);

        private final int value;

        CloudService(int value) {
            this.value = value;
        }

    }

    @Getter
    enum MsgCloudService {
        ALIYUN(1),
        QCLOUD(2);

        private final int value;

        MsgCloudService(int value) {
            this.value = value;
        }

    }
}
