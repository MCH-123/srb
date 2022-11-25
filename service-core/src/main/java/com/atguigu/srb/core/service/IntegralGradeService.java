package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 积分等级表 服务类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
public interface IntegralGradeService extends IService<IntegralGrade> {
    //积分等级分页查询
    IPage<IntegralGrade> getPageList(Page<IntegralGrade> page);
}
