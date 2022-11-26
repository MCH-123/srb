package com.atguigu.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @ClassName ServiceSmsApplication
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */

@SpringBootApplication
@EnableFeignClients
@ComponentScan({"com.atguigu.srb","com.atguigu.common"})
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
