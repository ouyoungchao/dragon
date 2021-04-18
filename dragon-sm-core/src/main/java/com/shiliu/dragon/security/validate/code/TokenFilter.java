package com.shiliu.dragon.security.validate.code;

import com.shiliu.dragon.untils.cache.SessionCache;
import com.shiliu.dragon.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * 短息验证码拦截器，校验短信
 */
public class TokenFilter
        extends OncePerRequestFilter implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private SimpleUrlAuthenticationFailureHandler myAuthenticationFailureHandler;


    private Set<String> urls = new HashSet<String>();

    private Set<String> certificationFreeUrls = new HashSet<>();

    private AntPathMatcher pathMatch = new AntPathMatcher();

    private SecurityProperties securityProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    //在其他属性设置完毕后，添加自己的拦截url
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        urls.add("/dragon/user");
        urls.add("/dragon/authentication");
        certificationFreeUrls.add("/dragon/user/register");
        certificationFreeUrls.add("/dragon/authentication/mobile");
        certificationFreeUrls.add("/dragon/authentication/user");
        certificationFreeUrls.add("/dragon/code/sms");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("TokenFilter begin doFilterInternal {}",request.getRequestURI());
        //new Exception("SmsCodeFilter").printStackTrace();
        boolean flag = false;
        for (String url : urls) {
            if (pathMatch.matchStart(request.getRequestURI(),url) && !certificationFreeUrls.contains(request.getRequestURI())) {
                flag = true;
                break;
            }
        }
        if (flag) {
            ServletWebRequest servletWebRequest = new ServletWebRequest(request);
            if (!existToken(servletWebRequest)) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, null);
                return;
            }
			String token = request.getHeader("token");
			String id = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
            Object object = SessionCache.getValueFromCache(id);
            if(object == null){
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, null);
                return;
            }
            Authentication authentication = (Authentication) object;
                    // TODO: 2021/4/6 更新后新增到缓存中
            SessionCache.addSession(token, authentication);
            logger.debug("get authentication {}",authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }else if (!certificationFreeUrls.contains(request.getRequestURI())){
            logger.warn("The url {} no need auth",request.getRequestURI());
            myAuthenticationFailureHandler.onAuthenticationFailure(request, response, null);
            return;
        }else {
            //查看是否已经登录过，已经登录过的直接更新token,不重复认证
            filterChain.doFilter(request, response);
        }
    }

    private boolean existToken(ServletWebRequest request) throws ServletRequestBindingException {
        //imageCode为login.html中的值
        String token = request.getHeader("token");
        //判断验证码的逻辑
        if (StringUtils.isBlank(token)) {
            logger.warn("The token is error");
            return false;
        }
		return true;

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
