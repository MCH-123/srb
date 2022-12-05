package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
public interface BorrowInfoService extends IService<BorrowInfo> {
    //根据id获取借款额度
    BigDecimal getBorrowAmount(Long userId);
    //获取借款人信息
    List<BorrowInfo> selectList();

    void saveBorrowInfo(BorrowInfo borrowInfo, Long userId);
}
