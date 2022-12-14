package com.atguigu.srb.core.controller.api;


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
import java.math.BigDecimal;
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
@Api(tags = "标的")
@RestController
@RequestMapping("api/core/lend")
@Slf4j
public class ApiLendController {
    @Resource
    private LendService lendService;

    @ApiOperation("获取标的列表")
    @GetMapping("list")
    public R list() {
        List<Lend> lends = lendService.selectList();
        return R.ok().data("lendList", lends);
    }

    @ApiOperation("获取标的信息")
    @GetMapping("show/{id}")
    public R show(
            @ApiParam(value = "标的id",required = true)
            @PathVariable Long id
    ) {
        Map<String, Object> lendDetail = lendService.getLendDetail(id);
        return R.ok().data("lendDetail", lendDetail);
    }

    @ApiOperation("计算投资收益")
    @GetMapping("/getInterestCount/{invest}/{yearRate}/{totalMonth}/{returnMethod}")
    public R getInterestCount(
            @ApiParam(value = "投资金额",required = true)
            @PathVariable BigDecimal invest,
            @ApiParam(value = "年化收益",required = true)
            @PathVariable BigDecimal yearRate,
            @ApiParam(value = "期数",required = true)
            @PathVariable Integer totalMonth,
            @ApiParam(value = "还款方式",required = true)
            @PathVariable Integer returnMethod
            ) {
        BigDecimal interestCount = lendService.getInterestCount(invest, yearRate, totalMonth, returnMethod);
        return R.ok().data("interestCount", interestCount);
    }

}

