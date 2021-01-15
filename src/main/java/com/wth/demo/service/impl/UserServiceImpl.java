package com.wth.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wth.demo.constants.Constant;
import com.wth.demo.entity.SysUser;
import com.wth.demo.exception.BusinessException;
import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.mapper.SysUserMapper;
import com.wth.demo.service.UserService;
import com.wth.demo.utils.JwtTokenUtil;
import com.wth.demo.utils.PageUtil;
import com.wth.demo.utils.PasswordUtils;
import com.wth.demo.vo.req.LoginReqVo;
import com.wth.demo.vo.req.UserPageVo;
import com.wth.demo.vo.resp.LoginRespVo;
import com.wth.demo.vo.resp.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public LoginRespVo login(LoginReqVo vo) {

        SysUser sysUser = sysUserMapper.selectByUsername(vo.getUsername());
        if (sysUser==null){
            throw new BusinessException(BaseResponseCode.ACCOUT_ERROT);
        }
        if (sysUser.getStatus() == 2){
            throw new BusinessException(BaseResponseCode.ACCOUT_LOCK_TIP);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(),vo.getPassword(),sysUser.getPassword())){
            throw new BusinessException(BaseResponseCode.ACCOUT_PASSWORD_ERROR);
        }
        //签发token 业务token负载
        Map<String,Object> claims = new HashMap<>();
        //用户名
        claims.put(Constant.JWT_USER_NAME,sysUser.getUsername());
        //角色
        claims.put(Constant.ROLES_INFOS_KEY,getRoleByUserId(sysUser.getId()));
        //权限
        claims.put(Constant.PERMISSION_INFOS_KEY,getPermissionByUserId(sysUser.getId()));
        String accessToken = JwtTokenUtil.getAccessToken(sysUser.getId(),claims);

        //刷新负载
        Map<String,Object> refreshClaims = new HashMap<>();
        refreshClaims.put(Constant.JWT_USER_NAME,sysUser.getUsername());

        log.info("accessToken{}",accessToken);
        String refreshToken = null;
        if (vo.getType().equals("1")){
            refreshToken = JwtTokenUtil.getRefreshToken(sysUser.getId(),refreshClaims);
        }
        if (vo.getType().equals("2")){
            refreshToken = JwtTokenUtil.getRefreshAppToken(sysUser.getId(),refreshClaims);
        }
        log.info("refreshToken{}",refreshToken);

        LoginRespVo loginRespVo = new LoginRespVo();
        loginRespVo.setToken(accessToken);
        loginRespVo.setRefresh_token(refreshToken);
        loginRespVo.setUserId(sysUser.getId());


        return loginRespVo;
    }

    /**
     * 分页查询
     * @param pageVo
     * @return
     */
    @Override
    public PageInfo<SysUser> pageInfo(UserPageVo pageVo) {

        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<SysUser> sysUsers = sysUserMapper.selectAll();
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);

        return pageInfo;
    }

    @Override
    public PageVo<SysUser> page(UserPageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<SysUser> list = sysUserMapper.selectAll();

        return PageUtil.getPageVo(list);
    }

    /**
     * 通过用户id查询拥有的角色信息
     * @param userId
     * @return
     */
    private List<String>  getRoleByUserId(String userId){
        List<String> list = new ArrayList<>();
        if (userId.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")){
            list.add("admin");
        }else {
            list.add("test");
        }
        return list;
    }

    private List<String> getPermissionByUserId(String userId){
        List<String> list = new ArrayList<>();
        if (userId.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")){
            list.add("sys:user:add");
            list.add("sys:user:edit");
            list.add("sys:user:delete");
            list.add("sys:user:list");
        }else {
            list.add("sys:user:add");
        }
        return list;
    }

}
