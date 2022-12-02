package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    List<BorrowerAttachVo> selectBorrowerAttachVOList(Long id);
}
