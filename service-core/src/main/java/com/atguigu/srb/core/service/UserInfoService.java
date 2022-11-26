package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
public interface UserInfoService extends IService<UserInfo> {
    //注册
    void register(RegisterVO registerVO);
    //登录
    UserInfoVO login(LoginVO loginVO, String ip);
    //分页
    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);
    //锁定
    void lock(Long id, Integer status);
    //校验
    boolean checkMobile(String mobile);
}
