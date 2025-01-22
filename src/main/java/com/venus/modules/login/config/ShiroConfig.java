package com.venus.modules.login.config;

import com.venus.common.constant.Constant;
import com.venus.modules.login.filter.KickoutSessionFilter;
import com.venus.modules.login.filter.OnlineSessionFilter;
import com.venus.modules.login.filter.SyncOnlineSessionFilter;
import com.venus.modules.login.oauth2.Oauth2Filter;
import com.venus.modules.login.oauth2.Oauth2Realm;
import com.venus.modules.login.session.CustomSessionDAO;
import com.venus.modules.login.session.CustomSessionFactory;
import com.venus.modules.login.session.CustomWebSessionManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public CustomSessionDAO sessionDAO() {
        return new CustomSessionDAO();
    }

    @Bean
    public CustomSessionFactory sessionFactory() {
        return new CustomSessionFactory();
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;
    }

    @Bean("sessionManager")
    public CustomWebSessionManager sessionManager() {
        CustomWebSessionManager sessionManager = new CustomWebSessionManager();

        // 禁用删除无效的session
        sessionManager.setDeleteInvalidSessions(true);

        // 设置全局session超时时间 30分钟
        sessionManager.setGlobalSessionTimeout(Constant.DEFAULT_SESSION_TIMEOUT);

        // 禁用url重写
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        // 启用session验证任务
        sessionManager.setSessionValidationSchedulerEnabled(true);

        // 设置sessionDAO
        sessionManager.setSessionDAO(sessionDAO());

        // 设置sessionFactory
        sessionManager.setSessionFactory(sessionFactory());

        sessionManager.setCacheManager(ehCacheManager());

        return sessionManager;
    }

    @Bean
    public KickoutSessionFilter kickoutSessionFilter() {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        kickoutSessionFilter.setMaxSession(1);
        kickoutSessionFilter.setCacheManager(ehCacheManager());
        kickoutSessionFilter.setSessionManager(sessionManager());

        return kickoutSessionFilter;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        // 启用session
        securityManager.setRememberMeManager(null);
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new Oauth2Filter());
        filters.put("onlineSession", onlineSessionFilter());
        filters.put("syncOnlineSession", syncOnlineSessionFilter());
        filters.put("kickout", kickoutSessionFilter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = getFilterMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 自定义在线用户处理过滤器
     */
    public OnlineSessionFilter onlineSessionFilter() {
        OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
        onlineSessionFilter.setCustomSessionDAO(sessionDAO());
        return onlineSessionFilter;
    }

    /**
     * 同步session到数据库
     */
    public SyncOnlineSessionFilter syncOnlineSessionFilter() {
        SyncOnlineSessionFilter syncOnlineSessionFilter = new SyncOnlineSessionFilter();
        syncOnlineSessionFilter.setCustomSessionDAO(sessionDAO());
        return syncOnlineSessionFilter;
    }

    @NotNull
    private static Map<String, String> getFilterMap() {
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/code", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/checkToken", "anon");
        filterMap.put("/codeLogin", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/doc.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/", "anon");
        filterMap.put("/**", "oauth2,kickout,onlineSession,syncOnlineSession");
        return filterMap;
    }
}
