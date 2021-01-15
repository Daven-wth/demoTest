package com.wth.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger2.enable}")
    private boolean enable;

    @Bean
    public Docket creayeRestApi(){
        /**
         * 添加头部信息
         */
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder accessTokenBuilder = new ParameterBuilder();
        ParameterBuilder refreshTokenBuilder = new ParameterBuilder();
        accessTokenBuilder.name("authorization").description("程序员自测的时候动态传输AccessToken 入口")
                .modelRef(new ModelRef("String")).parameterType("header").required(false);
        refreshTokenBuilder.name("refreshToken").description("程序员自测的时候动态传输RefreshToken 入口")
                .modelRef(new ModelRef("String")).parameterType("header").required(false);

        /**
         * 多个的时候就直接添加到pars就可以了
         */
        pars.add(accessTokenBuilder.build());
        pars.add(refreshTokenBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wth.demo.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .enable(enable);
//                .apis(R)
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("实战")
                .description("实战")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }





}
