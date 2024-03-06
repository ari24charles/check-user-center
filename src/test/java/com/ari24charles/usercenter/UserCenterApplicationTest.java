package com.ari24charles.usercenter;

import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot 测试类
 *
 * @author ari24charles
 */
@SpringBootTest
public class UserCenterApplicationTest {

    /**
     * 测试工具类中的 MD5 加密
     */
    @Test
    public void digestUtilTest() {
        String salt = "ac-user-center";
        String plaintext = "12345678";
        System.out.println(DigestUtil.md5Hex(salt + plaintext));
    }
}
