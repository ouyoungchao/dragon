package com.shiliu.dragon.security.authentication;

import com.shiliu.dragon.security.properties.UserProperties;
import com.shiliu.dragon.security.validate.AuthResponse;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {
    Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    @Autowired
    private DragonSessionAuthenticationStrategy sessionStrategy;

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher("/dragon/authentication/user", "POST"));
    }

    public UserAuthenticationFilter(String url,String httpMethod) {
        super(new AntPathRequestMatcher(url, httpMethod));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
       logger.info("UserAuthenticationFilter begin attemptAuthentication {}",request.getRequestURI());
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = "";
        String password = "";
        if(response.getHeader(UserProperties.CUSTOMERTAG) != null){
            username = UserProperties.VISITOR;
            password = UserProperties.VISITOR;
        }else{
            username = obtainUsername(request);
            password = obtainPassword(request);
        }
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException(JsonUtil.toJson(AuthResponse.USERNAME_PWD_ISEMPTY));
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        try {
            return super.getAuthenticationManager().authenticate(authRequest);
        }catch (BadCredentialsException exception){
            throw new BadCredentialsException(JsonUtil.toJson(AuthResponse.USERNAME_PWD_ERROR));
        }
    }


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("UserAuthenticationFilter begin doFilter");
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        if (!this.requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Request is to process authentication");
            }
            Authentication authResult;
            try {
                authResult = this.attemptAuthentication(request, response);
                if (authResult == null) {
                    return;
                }
                this.sessionStrategy.onAuthentication(authResult, request, response);
            } catch (InternalAuthenticationServiceException var8) {
                this.logger.error("An internal error occurred while trying to authenticate the user.", var8);
                this.unsuccessfulAuthentication(request, response, var8);
                return;
            } catch (AuthenticationException var9) {
                this.unsuccessfulAuthentication(request, response, var9);
                return;
            }
            this.successfulAuthentication(request, response, chain, authResult);
        }
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter).replaceAll(" ", "+");
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
