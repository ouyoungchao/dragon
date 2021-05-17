package com.shiliu.dragon.security.browser;

import com.shiliu.dragon.security.authentication.SmsCodeAuthenticationSecurityConfig;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.SmsCodeFilter;
import com.shiliu.dragon.security.validate.TokenFilter;
import com.shiliu.dragon.security.validate.ValidateCodeFilter;
import com.shiliu.dragon.security.authentication.UserAuthenticationFilter;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DragonSecurityConfig extends WebSecurityConfigurerAdapter{
	private Logger logger = LoggerFactory.getLogger(getClass());

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
	private SmsCodeFilter smsCodeFilter;

	@Autowired
	private ValidateCodeFilter validateCodeFilter;

	@Autowired
	private TokenFilter tokenFilter;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private UserAuthenticationFilter userAuthenticationFilter;

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
		logger.info("Enter BrowseSecurityConfig#config");
		//验证码逻辑
		validateCodeFilter.setAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();

		//短息验证码逻辑
		smsCodeFilter.setAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();

		//校验token
		tokenFilter.setMyAuthenticationFailureHandler(dragonAuthenticationFailureHandler);
		tokenFilter.setMyAuthenticationSuccessHandler(dragonAuthenticationSuccessHandler);
		tokenFilter.setSecurityProperties(securityProperties);
		tokenFilter.afterPropertiesSet();

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
				.loginPage("/login.html")
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
					"/dragon/user/register",
					"/dragon/content/publish",
					"/login.html"
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
