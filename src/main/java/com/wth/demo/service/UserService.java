package com.wth.demo.service;

import com.github.pagehelper.PageInfo;
import com.wth.demo.entity.SysUser;
import com.wth.demo.vo.req.LoginReqVo;
import com.wth.demo.vo.req.UserPageVo;
import com.wth.demo.vo.resp.LoginRespVo;
import com.wth.demo.vo.resp.PageVo;

public interface UserService {


    LoginRespVo login(LoginReqVo vo);

    PageInfo<SysUser> pageInfo(UserPageVo pageVo);

    PageVo<SysUser> page(UserPageVo pageVo);
}
