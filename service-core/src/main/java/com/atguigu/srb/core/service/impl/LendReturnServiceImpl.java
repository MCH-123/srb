package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.pojo.entity.LendReturn;
import com.atguigu.srb.core.mapper.LendReturnMapper;
import com.atguigu.srb.core.service.LendReturnService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * 还款记录表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
public class LendReturnServiceImpl extends ServiceImpl<LendReturnMapper, LendReturn> implements LendReturnService {

    @Override
    public List<LendReturn> selectByLendId(Long lendId) {

        return baseMapper.selectList(Wrappers.lambdaQuery(LendReturn.class)
                .eq(LendReturn::getLendId, lendId));
    }
}
