package com.shiliu.dragon.security.validate.code;

import com.shiliu.dragon.security.authentication.mobile.SmsCodeAuthenticationToken;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.code.ValidateCode;
import com.shiliu.dragon.security.validate.code.ValidateCodeController;
import com.shiliu.dragon.security.validate.code.ValidateCodeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 短息验证码拦截器，校验短信
 */
public class TokenFilter
				extends OncePerRequestFilter implements InitializingBean{

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@Autowired
	private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

	@Autowired
	private SimpleUrlAuthenticationFailureHandler myAuthenticationFailureHandler;

	
	private Set<String> urls = new HashSet<String>();
	
	private AntPathMatcher pathMatch = new AntPathMatcher();
	
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;
	
	//在其他属性设置完毕后，添加自己的拦截url
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		//urls.add("/dragon/user/register");
		urls.add("/dragon/user/1");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("TokenFilter begin doFilterInternal " + request.getRequestURI());
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
//				validate(servletWebRequest);

			} catch (ValidateCodeException e) {
				myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return ;
			}
			Authentication authentication = (Authentication)sessionStrategy.getAttribute(new ServletWebRequest(request),"session");
			sessionStrategy.setAttribute(new ServletWebRequest(request),"session",authentication);
			System.out.println("get authentication" + authentication);
			myAuthenticationSuccessHandler.onAuthenticationSuccess(request,response,authentication);
			//如果不是相应的请求，则直接调用相应的请求

		}

		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {

		//imageCode为login.html中的值
		String token = ServletRequestUtils.getStringParameter(request.getRequest(), "token");
		//判断验证码的逻辑
		if(StringUtils.isBlank(token)){
			throw new ValidateCodeException("请先登陆");
		}

	}
	
	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}
	
	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}


	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	public void setMyAuthenticationSuccessHandler(AuthenticationSuccessHandler myAuthenticationSuccessHandler) {
		this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
	}

	public void setMyAuthenticationFailureHandler(SimpleUrlAuthenticationFailureHandler myAuthenticationFailureHandler) {
		this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
	}
}
