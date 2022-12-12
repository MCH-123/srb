package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.mapper.LendItemMapper;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.atguigu.srb.core.service.*;
import com.atguigu.srb.core.util.LendNoUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {
    @Resource
    private LendMapper lendMapper;
    @Resource
    private LendService lendService;
    @Resource
    private UserAccountService userAccountService;
    @Resource
    private UserBindService userBindService;
    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private TransFlowService transFlowService;

    @Override
    public String commitInvest(InvestVO investVO) {

        //TODO:输入校验
        Long lendId = investVO.getLendId();
        //获取标的信息
        Lend lend = lendMapper.selectById(lendId);
        //募资状态
        Assert.isTrue(lend.getStatus().intValue() == LendStatusEnum.INVEST_RUN.getStatus().intValue(), ResponseEnum.LEND_INVEST_ERROR);
        //不能超卖
        BigDecimal sum = lend.getInvestAmount().add(new BigDecimal(investVO.getInvestAmount()));
        Assert.isTrue(sum.doubleValue() <= lend.getAmount().doubleValue(), ResponseEnum.LEND_FULL_SCALE_ERROR);
        //账户可用余额充足
        Long investUserId = investVO.getInvestUserId();
        BigDecimal amount = userAccountService.getAccount(investUserId);
        Assert.isTrue(amount.doubleValue() >= Double.parseDouble(investVO.getInvestAmount()), ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);
        //TODO:商户平台生成投资信息
        //标的下的投资信息
        LendItem lendItem = new LendItem();
        lendItem.setInvestUserId(investUserId);
        lendItem.setInvestName(investVO.getInvestName());
        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo);
        lendItem.setLendId(investVO.getLendId());
        lendItem.setInvestAmount(new BigDecimal(investVO.getInvestAmount()));
        lendItem.setLendYearRate(lend.getLendYearRate());
        lendItem.setInvestTime(LocalDateTime.now());
        lendItem.setLendStartDate(lend.getLendStartDate());
        lendItem.setLendEndDate(lend.getLendEndDate());
        //预期收益
        BigDecimal expectAmount = lendService.getInterestCount(lendItem.getInvestAmount(), lendItem.getLendYearRate(), lend.getPeriod(), lend.getReturnMethod());
        lendItem.setExpectAmount(expectAmount);
        //实际收益
        lendItem.setRealAmount(new BigDecimal(0));
        lendItem.setStatus(0);
        baseMapper.insert(lendItem);
        //TODO 组装投资相关参数 提交到汇付宝
        //获取投资人绑定协议号
        String bindCode = userBindService.getBindCodeByUserId(investUserId);
        //获取借款人绑定协议号
        String benefitBindCode = userBindService.getBindCodeByUserId(investUserId);

        //封装提交参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("voteBindCode", bindCode);
        paramMap.put("benefitBindCode", benefitBindCode);
        paramMap.put("agentProjectCode", lend.getLendNo());
        paramMap.put("agentProjectName", lend.getTitle());
        paramMap.put("agentBillNo", lendItemNo);
        paramMap.put("voteAmt", investVO.getInvestAmount());
        paramMap.put("votePrizeAmt", new BigDecimal(0));
        paramMap.put("voteFeeAmt", new BigDecimal(0));
        paramMap.put("projectAmt", lend.getAmount());
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);
        paramMap.put("timestamp", System.currentTimeMillis());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);
        return FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
    }

    @Override
    public void notify(Map<String, Object> map) {
        log.info("投标成功");
        //获取投资编号
        String agentBillNo = (String) map.get("agentBillNo");
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if (result) {
            log.warn("幂等性返回");
            return;
        }
        //获取用户的绑定协议号
        String voteBindCode = (String) map.get("voteBindCode");
        String voteAmt = (String) map.get("voteAmt");
        //修改账户金额
        userAccountMapper.updateAccount(voteBindCode, new BigDecimal("-" + voteAmt), new BigDecimal(voteAmt));
        //修改投资记录状态
        LendItem lendItem = this.getByLendItemNo(agentBillNo);
        lendItem.setStatus(1);
        baseMapper.updateById(lendItem);
        //修改标的信息
        Long lendId = lendItem.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        lend.setInvestNum(lend.getInvestNum()+1);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
        lendMapper.updateById(lend);
        //保存交易流水
        TransFlowBO transFlowBO = new TransFlowBO(agentBillNo, voteBindCode,
                new BigDecimal(voteAmt), TransTypeEnum.INVEST_LOCK,
                "投资项目编号:" + lend.getLendNo() + ",项目名称:" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);
    }

    @Override
    public List<LendItem> selectByLendId(Long lendId, Integer status) {
        return baseMapper.selectList(Wrappers.lambdaQuery(LendItem.class)
                .eq(LendItem::getLendId, lendId)
                .eq(LendItem::getStatus, status));
    }

    @Override
    public List<LendItem> selectByLendId(Long lendId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(LendItem.class)
        .eq(LendItem::getLendId,lendId)
        .eq(LendItem::getStatus,1));
    }

    private LendItem getByLendItemNo(String agentBillNo) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(LendItem.class)
                .eq(LendItem::getLendItemNo, agentBillNo));
    }
}
