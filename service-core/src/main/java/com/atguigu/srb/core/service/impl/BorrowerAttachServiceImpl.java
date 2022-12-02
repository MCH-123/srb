package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.mapper.BorrowerAttachMapper;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVo;
import com.atguigu.srb.core.service.BorrowerAttachService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
@Transactional
public class BorrowerAttachServiceImpl extends ServiceImpl<BorrowerAttachMapper, BorrowerAttach> implements BorrowerAttachService {

    @Override
    public List<BorrowerAttachVo> selectBorrowerAttachVOList(Long id) {

        List<BorrowerAttach> borrowerAttaches = baseMapper.selectList(new LambdaQueryWrapper<BorrowerAttach>()
                .eq(BorrowerAttach::getBorrowerId, id));
        ArrayList<BorrowerAttachVo> borrowerAttachVOList = new ArrayList<>();
        borrowerAttaches.forEach(borrowerAttach -> {
            BorrowerAttachVo borrowerAttachVo = new BorrowerAttachVo();
            borrowerAttachVo.setImageType(borrowerAttach.getImageType());
            borrowerAttachVo.setImageUrl(borrowerAttach.getImageUrl());
            borrowerAttachVOList.add(borrowerAttachVo);
        });
        return borrowerAttachVOList;
    }
}
