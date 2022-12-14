package com.atguigu.srb.core.controller.api;


import com.atguigu.common.result.R;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.core.pojo.vo.BorrowerVo;
import com.atguigu.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Api(tags = "借款人")
@RestController
@RequestMapping("api/core/borrower")
@Slf4j
public class ApiBorrowerController {
    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public R save(
            @RequestBody BorrowerVo borrowerVo,
            @RequestHeader(value = "token",required = false)
            String token
            ) {
        Long userId = JwtUtils.getUserId(token);
        borrowerService.saveBorrowerVoByUserId(borrowerVo, userId);
        return R.ok().message("信息提交成功");
    }

    @ApiOperation("获取借款人认证状态")
    @GetMapping("auth/getBorrowerStatus")
    public R getBorrowerStatus(@RequestHeader(value = "token", required = false) String token) {
        Long userId = JwtUtils.getUserId(token);
        Integer status = borrowerService.getStatusByUserId(userId);
        return R.ok().data("borrowerStatus", status);
    }
}

