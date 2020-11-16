package com.pagen.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.pagen.shiro.CustomerRealm;
import com.pagen.shiro.cache.RedisCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfigure {
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    //1.创建shiroFilter  //负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //配置系统受限资源
        //配置系统公共资源
        Map<String, String> map = new HashMap<String, String>();

        //设置权限 默认都可访问，后面特定的权限再添加
        map.put("/**", "anon");
//
//        map.put("/api/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/css/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/images/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/js/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/lib/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/mapper/**","anon");//anon 设置为公共资源  放行资源放在下面
//
//        map.put("/page/SQLStu/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/page/login.html**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/page/register.html","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/sys/**","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/user/getImage","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/#/register","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/user/registerview","anon");//anon 设置为公共资源  放行资源放在下面
//        map.put("/user/login","anon");//anon 设置为公共资源  放行资源放在下面
//
//        map.put("/**","anon");//authc 请求这个资源需要认证和授权

        //默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/index.html");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);


        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean("defaultWebSecurityManager")
    public DefaultSecurityManager getDefaultSecurityManager(Realm realm) {
        //给安全管理器设置Realm
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);

        //设置rememberMe
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName(realm.getName());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60);
        rememberMeManager.setCookie(cookie);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);

        return defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean("realm")
    public Realm getRealm() {
        CustomerRealm customerRealm = new CustomerRealm();

        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);


        //开启缓存管理
        customerRealm.setCacheManager(new RedisCacheManager());
        customerRealm.setCachingEnabled(true);//开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }

}
