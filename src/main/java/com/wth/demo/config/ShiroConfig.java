package com.wth.demo.config;

import com.wth.demo.shiro.CustomHashedCredentialsMatcher;
import com.wth.demo.shiro.CustomRealm;
import com.wth.demo.shiro.CustonAccessControlerFilter;
import com.wth.demo.shiro.ShiroCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class ShiroConfig {

    /**
     * 注入自定义cache
     * @return
     */
    @Bean
    public ShiroCacheManager cacheManager(){
        return new ShiroCacheManager();
    }

    /**
     * 自定义密码校验
     * @return
     */
    @Bean
    public CustomHashedCredentialsMatcher customHashedCredentialsMatcher(){
        return new CustomHashedCredentialsMatcher();

    }

    /**
     * 自定义域
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(customHashedCredentialsMatcher());
        //新增cache
        customRealm.setCacheManager(cacheManager());
        return customRealm;
    }

    /**
     * 配置安全管理器
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(customRealm());
        return webSecurityManager;
    }

    /**
     * 配置核心shiro过滤器 配置拦截请求
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //有序 自定义拦截器限制并发人数
        LinkedHashMap<String, Filter> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("token",new CustonAccessControlerFilter());
        shiroFilterFactoryBean.setFilters(linkedHashMap);

        //有序进行判断 不会拦截的连接
        LinkedHashMap<String,String> filterMap = new LinkedHashMap<>();
        //开放类型 anno
        filterMap.put("/api/user/login","anon");
        //放开swagger-ui地址
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        //druid sql监控配置
        filterMap.put("/druid/**", "anon");

        //需要进行token用户认证 授权认证
        filterMap.put("/**","token,authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }




}

