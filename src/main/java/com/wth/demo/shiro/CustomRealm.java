package com.wth.demo.shiro;

import com.wth.demo.constants.Constant;
import com.wth.demo.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;

public class CustomRealm extends AuthorizingRealm {

    /**
     * 验证令牌
     * @param token
     * @return
     */
    public boolean supports(AuthenticationToken token){
        return token instanceof CustomPasswordToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取token
        String accessToken = (String) principalCollection.getPrimaryPrincipal();
        //解析token
        Claims claims = JwtTokenUtil.getClaimsFromToken(accessToken);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //判断角色权限
        if (claims.get(Constant.PERMISSION_INFOS_KEY)!=null){
            info.addStringPermissions((Collection<String>) claims.get(Constant.PERMISSION_INFOS_KEY));
        }
        //判断角色
        if (claims.get(Constant.ROLES_INFOS_KEY)!=null){
            info.addRoles((Collection<String>) claims.get(Constant.ROLES_INFOS_KEY));
        }
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //使用token进行认证 拿到主体的token
        CustomPasswordToken customPasswordToken = (CustomPasswordToken) authenticationToken;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(customPasswordToken.getPrincipal(),customPasswordToken.getCredentials(),this.getName());
        return info;
    }
}
