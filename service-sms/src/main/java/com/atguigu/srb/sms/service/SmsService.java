package com.atguigu.srb.sms.service;

import java.util.Map;

/**
 * @ClassName SmsService
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
public interface SmsService {
    void send(String mobile, String checkCode) throws Exception;
}
