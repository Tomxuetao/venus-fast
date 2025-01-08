package com.venus.modules.login.session;

import com.venus.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.shiro.session.mgt.SimpleSession;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomSession extends SimpleSession {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 登录IP地址
     */
    private String host;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 在线状态
     */
    private Integer status = Constant.OnlineStatus.ONLINE.getValue();

    @Getter
    private transient boolean attributeChanged = false;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    public void markAttributeChanged() {
        attributeChanged = true;
    }

    public void resetAttributeChanged() {
        attributeChanged = false;
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
    }

    @Override
    public Object removeAttribute(Object key) {
        return super.removeAttribute(key);
    }
}
