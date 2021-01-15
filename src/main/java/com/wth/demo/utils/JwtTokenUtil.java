package com.wth.demo.utils;


import com.wth.demo.constants.Constant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    private static String securityKey;

    private static Duration accessTockenExpireTime;

    private static Duration refreshTokenExpireTime;

    private static Duration refreshTokenExpireAppTime;
    //签发人
    private static String issuer;


    /**
     * 创建实例
     * @param tokenSetting
     */
    public static void setJwtProperties(TokenSetting tokenSetting){
        securityKey = tokenSetting.getSecretKey();
        accessTockenExpireTime = tokenSetting.getAccessTockenExpireTime();
        refreshTokenExpireAppTime = tokenSetting.getRefreshTokenExpireAppTime();
        refreshTokenExpireTime = tokenSetting.getRefreshTokenExpireTime();
        issuer = tokenSetting.getIssuer();
    }

    /**
     * 生成access_token
     * @param subject
     * @param claims
     * @return
     */
    public static String getAccessToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,accessTockenExpireTime.toMillis(),securityKey);
    }

    /**
     * 生成PC端 refresh_token （PC端 过期时间短一些）
     * @param subject
     * @param claims
     * @return
     */
    public static String getRefreshToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireTime.toMillis(),securityKey);
    }

    /**
     * 生成App端 refresh_token （App端 过期时间长一些）
     * @param subject
     * @param claims
     * @return
     */
    public static String getRefreshAppToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireAppTime.toMillis(),securityKey);
    }

    /**
     * 从令牌中获取数据声明
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(securityKey)).parseClaimsJws(token).getBody();
        }catch (Exception e){
            //检查是否系统签发的令牌
            if (e instanceof ClaimJwtException){  //是否属于这种异常
                claims = ((ClaimJwtException) e).getClaims();  //拿到负载里面的信息 返回
            }
        }
        return claims;
    }

    /**
     * 获取用户id
     * @param token
     * @return
     */
    public static String getUserId(String token){
        String userId = null;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        }catch (Exception e){
            log.error("error={}",e);
        }
        return userId;
    }


    /**
     * 签发token
     * @param issuer  签发人
     * @param subject 代表jwt的主题 即所有人 一般是用户id
     * @param claims 存储在jwt里面的信息 一般放些用户的权限/角色信息
     * @param ttlmillis  有效时间
     * @param secret
     * @return
     */
    public static String generateToken(String issuer, String subject, Map<String,Object>
                                       claims,long ttlmillis,String secret){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingkey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam("typ","JWT");
        if (null != claims){
            builder.setClaims(claims);
        }

        if (!StringUtils.isEmpty(subject)){
            builder.setSubject(subject);
        }

        if (!StringUtils.isEmpty(issuer)){
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlmillis >= 0){
            long expMillis = nowMillis + ttlmillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm,signingkey);
        return builder.compact();
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUserName(String token){
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(Constant.JWT_USER_NAME);
        }catch (Exception e){
            log.error("error={}",e);
        }
        return username;
    }

    /**
     * 验证token 是否过期（true：已过期，false：未过期）
     * @param token
     * @return
     */
    public static Boolean isTokenExpired(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        }catch (Exception e){
            log.error("error={}",e);
            return true;
        }
    }

    /**
     * 校验令牌（true:验证通过 false:验证失败）
     * @param token
     * @return
     */
    public static Boolean validateToken(String token){
        Claims claims = getClaimsFromToken(token);
        return (null!=claims&&!isTokenExpired(token));
    }

    /**
     * 获取token的剩余过期时间
     * @param token
     * @return
     */
    public static long getRemainingTime(String token){
        long result = 0L;
        try {
            long nowMillis = System.currentTimeMillis();
            result = getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        }catch (Exception e){
            log.error("error={}",e);
        }
        return result;
    }








}
