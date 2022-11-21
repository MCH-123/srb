package com.atguigu.srb.sms.service.impl;

import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.util.HttpUtils;
import com.atguigu.srb.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SmsServiceImpl
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(String mobile, String checkCode) throws Exception {
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + SmsProperties.APP_CODE);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", mobile);
        querys.put("param", "code:"+checkCode);
        querys.put("tpl_id", SmsProperties.TEMPLATE_ID);
        Map<String, String> bodys = new HashMap<>();

        HttpResponse response = HttpUtils.doPost(SmsProperties.HOST, SmsProperties.PATH, SmsProperties.METHOD, headers, querys, bodys);
        System.out.println(response.toString());
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        Gson gson = new Gson();
        HashMap map = gson.fromJson(data, HashMap.class);
        String code = (String) map.get("return_code");

        if ("10001".equals(code)) {
            log.error("手机号码格式错误");
            throw new BusinessException(ResponseEnum.MOBILE_ERROR);
        }

        if (!"00000".equals(code)) {
            log.error("短信发送失败 " + " - code: " + code);
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR);
        }
    }
}
