package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.result.R;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Api(tags = "标的管理")
@RestController
@RequestMapping("admin/core/lend")
@Slf4j
public class AdminLendController {

    @Resource
    private LendService lendService;

    @ApiOperation("标的列表")
    @GetMapping("list")
    public R list() {
        List<Lend> lends = lendService.selectList();
        return R.ok().data("list", lends);
    }

    @ApiOperation("获取标的详情")
    @GetMapping("show/{id}")
    public R show(@ApiParam(value = "标的id",required = true) @PathVariable Long id) {
        Map<String,Object> result = lendService.getLendDetail(id);
        return R.ok().data("lendDetail", result);
    }

    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public R makeLoan(@ApiParam(value = "标的id", required = true) @PathVariable Long id) {
        lendService.makeLoan(id);
        return R.ok().message("放款成功");
    }
}

