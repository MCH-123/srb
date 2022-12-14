package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.common.result.R;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Api(tags = "会员账户")
@Slf4j
@RestController
@RequestMapping("api/core/userAccount")
public class ApiUserAccountController {
    @Resource
    private UserAccountService userAccountService;

    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(@ApiParam(value = "充值金额", required = true)
                          @PathVariable BigDecimal chargeAmt,
                          @RequestHeader("token") String token) {
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("用户充值回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("充值异步回调:" + JSON.toJSONString(paramMap));
        //验签
        if (RequestHelper.isSignEquals(paramMap)) {
            //充值成功
            if ("0001".equals(paramMap.get("resultCode"))) {
                return userAccountService.notify(paramMap);
            } else {
                log.info("充值异步回调失败:" + JSON.toJSONString(paramMap));
                return "success";
            }

        } else {
            log.info("用户签名错误:" + JSON.toJSONString(paramMap));
            return "fail";
        }
    }

    @ApiOperation("查询账户余额")
    @GetMapping("/auth/getAccount")
    public R getAccount(@RequestHeader(value = "token",required = false) String token) {
        Long userId = JwtUtils.getUserId(token);
        BigDecimal account = userAccountService.getAccount(userId);
        return R.ok().data("account", account);
    }

    @ApiOperation("用户提现")
    @PostMapping("/auth/commitWithdraw/{fetchAmt}")
    public R commitWithdraw(@ApiParam(value = "金额") @PathVariable BigDecimal fetchAmt,
                            @RequestHeader(value = "token", required = false)
                            String token
                            ) {
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitWithdraw(userId, fetchAmt);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("用户提现回调")
    @PostMapping("/notifyWithdraw")
    public String notifyWithdraw(HttpServletRequest request) {
        Map<String, Object> param = RequestHelper.switchMap(request.getParameterMap());
        log.info("提现异步回调:"+JSON.toJSONString(param));
        //验签
        if (RequestHelper.isSignEquals(param)) {
            //提现成功交易
            if ("0001".equals(param.get("resultCode"))) {
                userAccountService.notifyWithdraw(param);
            } else {
                log.info("提现异步回调充值失败：" + JSON.toJSONString(param));
                return "fail";
            }
        } else {
            log.info("提现异步回调签名错误：" + JSON.toJSONString(param));
            return "fail";
        }
        return "success";
    }

}

