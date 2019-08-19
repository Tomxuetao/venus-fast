package com.venus;

import com.venus.common.utils.RedisUtils;
import com.venus.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void redisListTest() {
        List<SysUserEntity> sysUserEntityList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SysUserEntity user = new SysUserEntity();
            user.setEmail("qqq@qq.com");
            user.setMobile("155555030339");
            redisUtils.set("user", user);
            sysUserEntityList.add(user);
        }
        redisUtils.leftPushAll("redis:user", Collections.singletonList(sysUserEntityList));
    }

}
