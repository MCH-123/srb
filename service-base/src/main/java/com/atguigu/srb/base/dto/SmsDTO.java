package com.atguigu.srb.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mch
 * @since 2022/12/15
 */
@Data
@ApiModel(description = "短信")
public class SmsDTO {
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("消息内容")
    private String message;
}
