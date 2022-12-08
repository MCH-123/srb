package com.atguigu.srb.core.controller.api;


import com.atguigu.common.result.R;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.pojo.entity.LendItemReturn;
import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.atguigu.srb.core.service.LendItemReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标的出借回款记录表 前端控制器
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Api("回款计划")
@Slf4j
@RestController
@RequestMapping("api/core/lendItemReturn")
public class ApiLendItemReturnController {
    @Resource
    private LendItemReturnService lendItemReturnService;

    @ApiOperation("获取列表")
    @GetMapping("auth/list/{lendId}")
    public R list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId,
            @RequestHeader(value = "token",required = false)
            String token
    ) {
        Long userId = JwtUtils.getUserId(token);
        List<LendItemReturn> lendItemReturnList = lendItemReturnService.selectByLendId(lendId,userId);
        return R.ok().data("list", lendItemReturnList);
    }
}

