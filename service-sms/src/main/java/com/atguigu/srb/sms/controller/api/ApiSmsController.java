package com.atguigu.srb.sms.controller.api;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.RandomUtils;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.sms.client.CoreUserInfoClient;
import com.atguigu.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ApiSmsController
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin
@Slf4j
public class ApiSmsController {
    @Resource
    private SmsService smsService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private CoreUserInfoClient coreUserInfoClient;


    @ApiOperation("获取验证码")
    @GetMapping("send/{mobile}")
    public R send(@ApiParam(value = "手机号",required = true) @PathVariable String mobile) throws Exception {
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        //远程调用检查是否已注册
        boolean result = coreUserInfoClient.checkMobile(mobile);
        log.info("是否注册:{}",result);
        Assert.isTrue(!result,ResponseEnum.MOBILE_EXIST_ERROR);
//        生成验证码
        String code = RandomUtils.getFourBitRandom();
        //发送短信
        smsService.send(mobile, code);
        //将验证码存入redis
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 5, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功");
    }
}
