package com.wth.demo.controller;


import com.github.pagehelper.PageInfo;
import com.wth.demo.entity.SysUser;
import com.wth.demo.service.UserService;
import com.wth.demo.utils.DataResult;
import com.wth.demo.vo.req.LoginReqVo;
import com.wth.demo.vo.req.UserPageVo;
import com.wth.demo.vo.resp.LoginRespVo;
import com.wth.demo.vo.resp.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Api(tags = "用户描述")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    @ApiModelProperty(value = "登录")
    public DataResult<LoginRespVo> login(@RequestBody @Valid LoginReqVo reqVo){
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.login(reqVo));

        return dataResult;

    }

    @PostMapping("/users")
    @ApiModelProperty(value = "分页用户查询")
    @RequiresPermissions("sys:user:list")
    public DataResult<PageInfo<SysUser>> pageInfo(@RequestBody UserPageVo vo){
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.pageInfo(vo));
        return dataResult;
    }


    @PostMapping("/user/page")
    @ApiModelProperty(value = "分页封装查询")
    public DataResult<PageVo<SysUser>> page(@RequestBody UserPageVo vo){
        DataResult result = DataResult.success();
        result.setData(userService.page(vo));
        return result;
    }




}
