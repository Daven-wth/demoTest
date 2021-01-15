package com.wth.demo.controller;


import com.wth.demo.exception.BusinessException;
import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.utils.DataResult;
import com.wth.demo.vo.req.TestReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.crypto.Data;

@RestController
@Api(tags = "测试接口",description = "测试用")
@RequestMapping("/test")
public class TestController {

    @GetMapping("/index")
    @ApiModelProperty(value = "引导页接口")
    public String testResult(){
        return "hello world";
    }

    @GetMapping("/home")
    @ApiModelProperty("测试DataReult接口")
    public DataResult<String> getHome(){
        int i = 1/0;
        DataResult<String> result = DataResult.success("aaaaaa");
        return result;
    }


    @GetMapping("/bussiness/error")
    @ApiModelProperty(value = "测试主动抛出业务异常接口")
    public DataResult testBusniessError(@RequestParam String type){
        if (!(type.equals("1"))){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        DataResult result = new DataResult(0,type);
        return result;
    }


    @PostMapping("test/vaild")
    @ApiModelProperty(value = "测试校验接口")
    public DataResult testValid(@RequestBody @Valid TestReqVo reqVo){
        DataResult result = DataResult.success(reqVo);
        return result;
    }
}
