package com.atguigu.srb.sms;

import com.atguigu.srb.sms.util.SmsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName UtilsTests
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
@SpringBootTest
class UtilsTests {
    @Test
    void testProperties() {
        System.out.println(SmsProperties.APP_CODE);
        System.out.println(SmsProperties.APP_SECRET);
        System.out.println(SmsProperties.TEMPLATE_ID);
    }
}
