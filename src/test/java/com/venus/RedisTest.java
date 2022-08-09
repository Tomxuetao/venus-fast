package com.venus;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void redisStringTest() {
        SysUserEntity user = new SysUserEntity();
        user.setEmail("qqq@qq.com");
        user.setMobile("155555030339");
        redisUtils.set("user", user);

        System.out.println(ToStringBuilder.reflectionToString(redisUtils.get("user", SysUserEntity.class)));

        System.out.println(redisUtils.get("user"));
    }

}
