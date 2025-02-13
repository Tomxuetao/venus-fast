package com.venus.common.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class GeneratorClientCredentials {
    // 生成 ClientId（唯一标识）
    public static String generateClientId() {
        // 使用 UUID 并移除连字符，生成 32 位唯一字符串
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 生成高强度的 ClientSecret
    public static String generateClientSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32]; // 256 位熵
        secureRandom.nextBytes(randomBytes);

        // 使用 URL 安全的 Base64 编码，并移除填充符号
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
