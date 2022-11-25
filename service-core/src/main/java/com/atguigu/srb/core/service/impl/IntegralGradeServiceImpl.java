package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.mapper.IntegralGradeMapper;
import com.atguigu.srb.core.service.IntegralGradeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分等级表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
public class IntegralGradeServiceImpl extends ServiceImpl<IntegralGradeMapper, IntegralGrade> implements IntegralGradeService {

    @Override
    public IPage<IntegralGrade> getPageList(Page<IntegralGrade> page) {
        return baseMapper.selectPage(page, null);
    }
}
