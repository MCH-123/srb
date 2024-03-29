package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {
    //获取前50条日志
    List<UserLoginRecord> listTop50(Long userId);
}
