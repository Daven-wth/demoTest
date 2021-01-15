package com.wth.demo.shiro;

import com.wth.demo.constants.Constant;
import com.wth.demo.exception.BusinessException;
import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.service.RedisService;
import com.wth.demo.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户认证匹配
 */
@Slf4j
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        CustomPasswordToken customPasswordToken = (CustomPasswordToken) token;
        String accessToken = (String) customPasswordToken.getCredentials();
        String userId = JwtTokenUtil.getUserId(accessToken);
        log.info("doCredentialsMatch.....userId:{}",userId);
        if (redisService.hasKey(Constant.DELETED_USER_KEY+userId)){
            throw new BusinessException(BaseResponseCode.ACCOUNT_DELETED_ERROR);
        }
        //是否被锁定
        if (redisService.hasKey(Constant.ACCOUNT_LOCK_KEY+userId)){
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK);
        }
        //判断token失效
        if (!JwtTokenUtil.validateToken(accessToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_PASE_DUE);
        }
        return true;
    }
}
