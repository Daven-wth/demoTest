package com.wth.demo.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRespVo {

    @ApiModelProperty(value = "业务token")
    private String token;

    @ApiModelProperty(value = "刷新token")
    private String refresh_token;

    @ApiModelProperty(value = "用户id")
    private String userId;





}
