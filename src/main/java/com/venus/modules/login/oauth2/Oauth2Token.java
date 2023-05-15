package com.venus.modules.login.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

public class Oauth2Token implements AuthenticationToken {
    private final String token;

    public Oauth2Token(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
