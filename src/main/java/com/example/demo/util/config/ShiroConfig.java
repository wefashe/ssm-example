package com.example.demo.util.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.util.shiro.ShiroRealm;

@Configuration
public class ShiroConfig {

	@Autowired
	private CustomPropertis custom;

	/**
	 * 密码校验规则HashedCredentialsMatcher 这个类是为了对密码进行编码的 , 防止密码在数据库里明码保存 , 当然在登陆认证的时候 ,
	 * 这个类也负责对form里输入的密码进行编码 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		// 指定加密方式为MD5
		credentialsMatcher.setHashAlgorithmName("MD5");
		// 加密次数
		credentialsMatcher.setHashIterations(1024);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	@Bean
	public ShiroRealm shiroRealm(HashedCredentialsMatcher matcher) {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setAuthorizationCachingEnabled(false);
		shiroRealm.setCredentialsMatcher(matcher);
		return shiroRealm;
	}

	@Bean
	public SecurityManager securityManager(ShiroRealm shiroRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm);
		return securityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		shiroFilterFactoryBean.setLoginUrl(custom.getShiro().getLoginUrl());
		shiroFilterFactoryBean.setSuccessUrl(custom.getShiro().getSuccessUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(custom.getShiro().getUnauthorizedUrl());

		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 定义filterChain，静态资源不拦截
		filterChainDefinitionMap.put("/guest/**", "anon");
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/static/**", "anon");
		// druid数据源监控页面不拦截
		filterChainDefinitionMap.put("/druid/**", "anon");
		// 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/", "anon");
		// 除上以外所有url都必须认证通过才可以访问，未通过认证自动访问LoginUrl
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	// @Bean
	// public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
	// return new LifecycleBeanPostProcessor();
	// }

}
