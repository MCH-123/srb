package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AdminIntegralGradeController
 * @Description TODO
 * @Author mch
 * @Date 2022/11/13
 * @Version 1.0
 */
//@CrossOrigin
@Slf4j
@RestController
@Api(tags = "积分等级管理")
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("list")
    public R listAll() {
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list);
    }

    @ApiOperation("积分等级分页列表")
    @GetMapping("{currentPage}/{pageSize}")
    public R pageList(@PathVariable Integer currentPage,@PathVariable Integer pageSize ) {
//        List<IntegralGrade> list = integralGradeService.list();
        Page<IntegralGrade> page = new Page<>(currentPage, pageSize);
        IPage<IntegralGrade> pageList = integralGradeService.getPageList(page);
        return R.ok().data("list", pageList);
    }

    @ApiOperation(value = "根据id删除积分等级", notes = "逻辑删除")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "数据id", required = true, example = "100") @PathVariable Long id) {
        boolean result = integralGradeService.removeById(id);
        if (result) return R.ok().message("删除成功");
        return R.error().message("删除失败");
    }

    @ApiOperation("新增积分等级")
    @PostMapping("save")
    public R save(@ApiParam(value = "积分等级对象", required = true) @RequestBody IntegralGrade integralGrade) {
//        if (integralGrade.getBorrowAmount() == null) throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        Assert.notNull(integralGrade.getBorrowAmount(), ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        boolean result = integralGradeService.save(integralGrade);
        if (result) return R.ok().message("保存成功");
        return R.error().message("保存失败");
    }

    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "数据id", required = true, example = "1") @PathVariable Long id) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade != null) {
            return R.ok().data("record", integralGrade);
        }
        return R.ok().message("数据不存在");
    }

    @ApiOperation("更新积分等级")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "积分等级对象", required = true)
                        @RequestBody IntegralGrade integralGrade
    ) {
        boolean result = integralGradeService.updateById(integralGrade);
        if (result) return R.ok().message("修改成功");
        return R.error().message("修改失败");
    }
}
