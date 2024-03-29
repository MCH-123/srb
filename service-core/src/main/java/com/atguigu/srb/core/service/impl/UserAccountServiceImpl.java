package com.atguigu.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.base.dto.SmsDTO;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.TransFlow;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.atguigu.srb.core.service.UserAccountService;
import com.atguigu.srb.core.service.UserBindService;
import com.atguigu.srb.core.service.UserInfoService;
import com.atguigu.srb.core.util.LendNoUtils;
import com.atguigu.srb.rabbitutil.constant.MQConst;
import com.atguigu.srb.rabbitutil.service.MQService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author mch
 * @since 2022-11-13
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private UserBindService userBindService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MQService mqService;
    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();
        //判断用户绑定状态
        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("chargeAmt", chargeAmt);
        paramMap.put("feeAmt", new BigDecimal("0"));
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);//检查常量是否正确
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        return FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
    }

    @Override
    public String notify(Map<String, Object> paramMap) {
        log.info("充值成功:"+ JSONObject.toJSONString(paramMap));
        boolean isSave = transFlowService.isSaveTransFlow((String) paramMap.get("agentBillNo"));
        if (isSave) {
            log.warn("幂等性返回");
            return "fail";
        }
        String bindCode = (String) paramMap.get("bindCode");
        String chargeAmt = (String) paramMap.get("chargeAmt");
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));
        //保存交易流水
        String agentBillNo = (String) paramMap.get("agentBillNo");
        TransFlowBO transFlowBo = new TransFlowBO(agentBillNo, bindCode, new BigDecimal(chargeAmt), TransTypeEnum.RECHARGE, "充值");
        transFlowService.saveTransFlow(transFlowBo);
        //发消息
        log.info("发消息");
        String mobile = userInfoService.getMobileByBindCode(bindCode);
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setMobile(mobile);
        smsDTO.setMessage("充值成功");
        mqService.sendMessage(MQConst.EXCHANGE_TOPIC_SMS, MQConst.ROUTING_SMS_ITEM, smsDTO);
        return "success";
    }

    @Override
    public BigDecimal getAccount(Long userId) {
        UserAccount userAccount = baseMapper.selectOne(Wrappers.lambdaQuery(UserAccount.class)
                .select(UserAccount::getAmount)
                .eq(UserAccount::getUserId, userId));
        return userAccount.getAmount();
    }

    @Override
    public String commitWithdraw(Long userId, BigDecimal fetchAmt) {
        //账户可用余额充足
        BigDecimal amount = this.getAccount(userId);
        Assert.isTrue(amount.doubleValue()>=fetchAmt.doubleValue(),ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);
        String bindCode = userBindService.getBindCodeByUserId(userId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getWithdrawNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("fetchAmt", fetchAmt);
        paramMap.put("feeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", HfbConst.WITHDRAW_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.WITHDRAW_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);
        return FormHelper.buildForm(HfbConst.WITHDRAW_URL, paramMap);
    }

    @Override
    public void notifyWithdraw(Map<String, Object> param) {
        log.info("提现成功");
        String agentBillNo = (String) param.get("agentBillNo");
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if (result) {
            log.warn("幂等性返回");
            return;
        }
        String bindCode = (String)param.get("bindCode");
        String fetchAmt = (String)param.get("fetchAmt");

        //根据用户账户修改账户金额
        baseMapper.updateAccount(bindCode, new BigDecimal("-" + fetchAmt), new BigDecimal(0));

        //增加交易流水
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(fetchAmt),
                TransTypeEnum.WITHDRAW,
                "提现");
        transFlowService.saveTransFlow(transFlowBO);
    }
}
