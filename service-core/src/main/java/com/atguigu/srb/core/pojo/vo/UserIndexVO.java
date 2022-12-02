package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mch
 * @since 2022/11/29
 */
@Data
@ApiModel(description = "用户账户信息")
public class UserIndexVO {
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户昵称")
    private String nickName;
    @ApiModelProperty("上次登录时间")
    private LocalDateTime lastLoginTime;
    @ApiModelProperty("头像")
    private String headImg;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("用户类型")
    private Integer userType;
    @ApiModelProperty("绑定状态")
    private Integer bindStatus;
    @ApiModelProperty("账户余额")
    private BigDecimal amount;
    @ApiModelProperty("冻结金额")
    private BigDecimal freezeAmount;
}
