package com.shiliu.dragon.security.validate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shiliu.dragon.utils.cache.SessionCache;
import com.shiliu.dragon.security.properties.SmsCodeProperties;
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
 * 短息验证码拦截器，校验短信
 */
@Component
public class SmsCodeFilter 
				extends OncePerRequestFilter implements InitializingBean{
	Logger logger = LoggerFactory.getLogger(getClass());

	//自定义失败异常，MyAuthenticationFailureHandler
	private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;
	private Set<String> urls = new HashSet<String>();
	private AntPathMatcher pathMatch = new AntPathMatcher();

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private ObjectMapper objectMap;

	@Autowired
	private RedisTemplate redisTemplate;
	
	//在其他属性设置完毕后，添加自己的拦截url
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		urls.add(SmsCodeProperties.AUTHMOBILE);
		urls.add(SmsCodeProperties.LOGINMOBILE);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("SmsCodeFilter begin doFilterInternal {}",request.getRequestURI());
		//new Exception("SmsCodeFilter").printStackTrace();
		boolean flag = false;
		for(String url : urls){
			if(pathMatch.match(url, request.getRequestURI())){
				flag = true;
			}
		}
		
		//处理/authentication/mobile的请求
		if(flag){
			try {
				ServletWebRequest servletWebRequest = new ServletWebRequest(request);
				validate(servletWebRequest);
			} catch (ValidateCodeException e) {
				logger.error("Do Filter with ValidateCodeException ",e);
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return ;
			}
		}
		//如果不是相应的请求，则直接调用相应的请求
		filterChain.doFilter(request, response);
	}

	public void validate(ServletWebRequest request) throws ServletRequestBindingException, IOException {
		String mobile = ServletRequestUtils.getStringParameter(request.getRequest(), "mobile");
		if(mobile == null || mobile.trim().length() != 13){
			throw new ValidateCodeException("");
		}
		//系统生成值
		ValidateCode codeInSession = (ValidateCode) redisTemplate.opsForValue().get(mobile);
		//请求参数值
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");
		//验证码为空
		if(StringUtils.isBlank(codeInRequest)){
			logger.warn("Sms code is empty");
			throw new ValidateCodeException("");
		}
		//验证码不存在
		if(codeInSession == null){
			logger.warn("Sms code not exit");
			throw new ValidateCodeException("");
		}
		//验证码已经过期
		if(codeInSession.isExpired()){
			logger.warn("Sms is expired");
			throw new ValidateCodeException("");
		}
		//验证码不匹配
		if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
			logger.warn("codeInSession =  {} and codeInRequest = {}",codeInSession.getCode(),codeInRequest);
			throw new ValidateCodeException("");
		}
		redisTemplate.delete(mobile);
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

	public ObjectMapper getObjectMap() {
		return objectMap;
	}

	public void setObjectMap(ObjectMapper objectMap) {
		this.objectMap = objectMap;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
