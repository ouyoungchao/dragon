package com.shiliu.dragon.security.browser;

import com.shiliu.dragon.security.authentication.mobile.SmsCodeAuthenticationProvider;
import com.shiliu.dragon.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.code.SmsCodeFilter;
import com.shiliu.dragon.security.validate.code.TokenFilter;
import com.shiliu.dragon.security.validate.code.ValidateCodeFilter;
import com.shiliu.dragon.security.authentication.mobile.UserAuthenticationFilter;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;


@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public PasswordEncoder passwordEncoder(){
		//根据加密方式不同获取的encoder
		return new BCryptPasswordEncoder();
	};

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationSuccessHandler dragonAuthenticationSuccessHandler;
	
	@Autowired
	private SimpleUrlAuthenticationFailureHandler dragonAuthenticationFailureHandler;

	private DataSource dataSource = new DataSource();
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SpringSocialConfigurer socialSecurityConfig;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Bean
	public PersistentTokenRepository tokenRepository(){
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		//第二从启动时设置为false
		tokenRepository.setCreateTableOnStartup(false);
		return tokenRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("enter BrowseSecurityConfig#config");
		//验证码逻辑
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		
		
		//短息验证码逻辑
		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();

		//校验token
		TokenFilter tokenFilter = new TokenFilter();
		tokenFilter.setMyAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		tokenFilter.setMyAuthenticationSuccessHandler(dragonAuthenticationSuccessHandler);
		tokenFilter.setSecurityProperties(securityProperties);
		tokenFilter.afterPropertiesSet();

		UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
		userAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		userAuthenticationFilter.setAuthenticationSuccessHandler(dragonAuthenticationSuccessHandler);
		userAuthenticationFilter.setAuthenticationFailureHandler(dragonAuthenticationFailureHandler);

		//基于security的基本配置
//		http.httpBasic()
		http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				//表单登录即认证
			.formLogin()
				//指定登录页面
				//.loginPage("/dragon/authentication/require")
				//表单登录时UsernamePasswordAuthenticationFilter处理这个请求
				.loginProcessingUrl("/dragon/authentication/user")
				//表单登陆后使用定义的处理器
				.successHandler(dragonAuthenticationSuccessHandler)
				.failureHandler(dragonAuthenticationFailureHandler)
			.and()
				.rememberMe()
				.tokenRepository(tokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService)
			//且	
			.and()
				.authorizeRequests()
				//给予以下url免身份认证
				.antMatchers("/dragon/authentication/require",
					//以html结尾的页面放权
					securityProperties.getBrowser().getLogin(),
					securityProperties.getBrowser().getSignup(),
					//图形验证码授权
					"/dragon/code/image",
					"/dragon/code/sms",
					"/dragon/user/register"
					/*"/dragon/user/register"*/).permitAll()
					//授权		
				.anyRequest()
				.authenticated()
			.and()
				//失去服务，跨站请求伪造
				.csrf().disable()
				.apply(smsCodeAuthenticationSecurityConfig);
		
	}
	
}
