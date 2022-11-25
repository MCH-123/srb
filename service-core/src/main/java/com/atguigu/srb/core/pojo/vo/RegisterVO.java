package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mch
 * @since 2022/11/21
 */
@Data
@ApiModel(description = "注册对象")
public class RegisterVO {
    @ApiModelProperty("用户类型")
    private Integer userType;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("密码")
    private String password;
}