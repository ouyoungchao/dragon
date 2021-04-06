package com.shiliu.dragon.security.validate.code;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiliu.dragon.common.cache.SessionCache;
import com.shiliu.dragon.common.utils.JsonUtil;
import com.shiliu.dragon.security.properties.SmsCodeProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shiliu.dragon.security.properties.SecurityProperties;

/**
 * 短息验证码拦截器，校验短信
 */
public class SmsCodeFilter 
				extends OncePerRequestFilter implements InitializingBean{


	//自定义失败异常，MyAuthenticationFailureHandler
	private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;
	
	private Set<String> urls = new HashSet<String>();
	
	private AntPathMatcher pathMatch = new AntPathMatcher();

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private ObjectMapper objectMap;
	
	//在其他属性设置完毕后，添加自己的拦截url
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		urls.add(SmsCodeProperties.AUTHMOBILE);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("SmsCodeFilter begin doFilterInternal ");
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
				validate(servletWebRequest,response);
			} catch (ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return ;
			}
		}
		//如果不是相应的请求，则直接调用相应的请求
		filterChain.doFilter(request, response);
	}

	public void validate(ServletWebRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException {
		String mobile = ServletRequestUtils.getStringParameter(request.getRequest(), "mobile");
		if(mobile == null || mobile.trim().length() != 13){
//			response.getWriter().write(JsonUtil.toJson(SmsResponse.INVALIDPARAM));
			throw new ValidateCodeException("");
		}
		//系统生成值
		ValidateCode codeInSession = (ValidateCode) SessionCache.getValueFromCache(mobile);
		//请求参数值
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");
		//验证码为空
		if(StringUtils.isBlank(codeInRequest)){
//			response.getWriter().write(JsonUtil.toJson(SmsResponse.SMSISEMPTY));
			throw new ValidateCodeException("");
		}
		//验证码不存在
		if(codeInSession == null){
//			response.getWriter().write(JsonUtil.toJson(SmsResponse.SMSNOTEXIST));
			throw new ValidateCodeException("");
		}
		//验证码已经过期
		if(codeInSession.isExpired()){
			SessionCache.removeFromCache(ValidateCodeController.SESSION_KEY);
//			response.getWriter().write(JsonUtil.toJson(SmsResponse.SMSEXPIRED));
			throw new ValidateCodeException("");
		}
		//验证码不匹配
		if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
			System.out.println("codeInSession = " + codeInSession.getCode() + " and codeInRequest = " + codeInRequest);
//			response.getWriter().write(JsonUtil.toJson(SmsResponse.SMSUNCORRECT));
			throw new ValidateCodeException("");
		}
		SessionCache.removeFromCache(ValidateCodeController.SESSION_KEY);
	}
	
	public SimpleUrlAuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
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
