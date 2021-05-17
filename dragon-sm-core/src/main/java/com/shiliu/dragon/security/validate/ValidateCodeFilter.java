package com.shiliu.dragon.security.validate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shiliu.dragon.security.properties.SecurityProperties;

/**
 * 登陆接口验证码拦截器
 */
@Component
public class ValidateCodeFilter 
				extends OncePerRequestFilter implements InitializingBean{
	private Logger logger = LoggerFactory.getLogger(getClass());

	//自定义失败异常，MyAuthenticationFailureHandler
	private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;
	
	private Set<String> urls = new HashSet<String>();
	
	private AntPathMatcher pathMatch = new AntPathMatcher();
	
	private SecurityProperties securityProperties;

	@Autowired
	private RedisTemplate redisTemplate;
	
	//在其他属性设置完毕后，添加自己的拦截url
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		urls.add("/dragon/authentication/form");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("ValidateCodeFilter begin doFilterInternal {}",request.getRequestURI());
		// TODO: 2021/3/22 该拦截器预留，先不做验证码校验
		//如果不是相应的请求，则直接调用相应的请求
		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		ImageCode codeInSession = (ImageCode) redisTemplate.opsForValue().get("SESSION_KEY_IMAGE_CODE");
		//imageCode为login.html中的值
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
		//判断验证码的逻辑
		if(StringUtils.isBlank(codeInRequest)){
			throw new ValidateCodeException("验证码不能为空");
		}
		if(codeInSession == null){
			throw new ValidateCodeException("验证码不存在");
		}
		if(codeInSession.isExpired()){
			redisTemplate.delete("SESSION_KEY_IMAGE_CODE");
			throw new ValidateCodeException("验证码已经过期");
		}
		if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
			throw new ValidateCodeException("验证码不匹配");
		}
		redisTemplate.delete("SESSION_KEY_IMAGE_CODE");
	}

	public void setAuthenticationFailureHandler(
			SimpleUrlAuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
}
