package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.pojo.entity.LendItemReturn;
import com.atguigu.srb.core.mapper.LendItemReturnMapper;
import com.atguigu.srb.core.service.LendItemReturnService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Override
    public List<LendItemReturn> selectByLendId(Long lendId, Long userId) {

        return baseMapper.selectList(Wrappers.lambdaQuery(LendItemReturn.class)
                .eq(LendItemReturn::getLendId, lendId)
                .eq(LendItemReturn::getInvestUserId, userId)
                .orderByAsc(LendItemReturn::getCurrentPeriod));
    }
}
