package com.wth.demo.vo.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TestReqVo {

    @NotEmpty(message = "list集合不能为空")
    @ApiModelProperty(value = "list集合数据")
    private List<String> list;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "年龄不能为空")
    @ApiModelProperty(value = "年龄")
    private String age;
}
