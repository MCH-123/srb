
package com.atguigu.srb.sms.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
//调用setter为成员赋值
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties implements InitializingBean {

    public  String appSecret;//
    public  String appCode;//
    public  String host;//
    public  String path;//
    public  String method;//
    public  String templateId;//

    public static String APP_SECRET;
    public static String APP_CODE;
    public static String HOST;
    public static String PATH;
    public static String METHOD;
    public static String TEMPLATE_ID;
    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        APP_SECRET = appSecret;
        APP_CODE = appCode;
        HOST = host;
        PATH = path;
        METHOD = method;
        TEMPLATE_ID = templateId;
    }
}