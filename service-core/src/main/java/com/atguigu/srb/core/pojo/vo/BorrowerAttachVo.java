package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mch
 * @since 2022/12/1
 */
@Data
@ApiModel("借款人附件资料")
public class BorrowerAttachVo {
    @ApiModelProperty("图片类型 （idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）")
    private String imageType;
    @ApiModelProperty("图片路径")
    private String imageUrl;
}
