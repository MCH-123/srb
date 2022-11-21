package com.atguigu.srb.oss;

import com.atguigu.srb.oss.util.OssProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName OssTest
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
@SpringBootTest
class OssTest {
    @Test
    void t1() {
        System.out.println(OssProperties.BUCKET_NAME);
    }
}
