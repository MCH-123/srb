package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.pojo.entity.UserIntegral;
import com.atguigu.srb.core.mapper.UserIntegralMapper;
import com.atguigu.srb.core.service.UserIntegralService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户积分记录表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
@Transactional
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralMapper, UserIntegral> implements UserIntegralService {

}
