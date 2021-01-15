package com.wth.demo.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class TokenSetting {

    private  String secretKey;

    private  Duration accessTockenExpireTime;

    private  Duration refreshTokenExpireTime;

    private  Duration refreshTokenExpireAppTime;

    private  String issuer;


}
