package com.wth.demo.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPageVo {

    @ApiModelProperty(value = "当前第几页")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "当前页条数")
    private Integer pageSize = 10;

}
