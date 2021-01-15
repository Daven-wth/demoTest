package com.wth.demo.utils;

import org.springframework.stereotype.Component;

/**
 * 初始化配置代理类
 */
@Component
public class InitializerUtil {

    public InitializerUtil(TokenSetting setting) {
        JwtTokenUtil.setJwtProperties(setting);
    }
}
