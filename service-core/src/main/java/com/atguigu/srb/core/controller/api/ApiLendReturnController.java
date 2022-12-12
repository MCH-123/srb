package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.common.result.R;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.atguigu.srb.core.service.LendReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Api(tags = "还款记录")
@RestController
@Slf4j
@RequestMapping("api/core/lendReturn")
public class ApiLendReturnController {
    @Resource
    private LendReturnService lendReturnService;

    @ApiOperation("获取列表")
    @GetMapping("list/{lendId}")
    public R list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId
    ) {
        List<LendReturn> lendReturnList = lendReturnService.selectByLendId(lendId);
        return R.ok().data("list", lendReturnList);
    }

    @ApiOperation("还款")
    @PostMapping("auth/commitReturn/{lendReturnId}")
    public R commitReturn(
            @ApiParam(value = "还款计划id")
            @PathVariable Long lendReturnId,
            @RequestHeader("token")
            String token
    ) {
        Long userId = JwtUtils.getUserId(token);
        String formStr = lendReturnService.commitReturn(userId, lendReturnId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("还款异步回调")
    @PostMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("还款异步回调"+ JSON.toJSONString(paramMap));
        //验签
        //校验签名
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendReturnService.notify(paramMap);
            } else {
                log.info("还款异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("还款异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }
}

