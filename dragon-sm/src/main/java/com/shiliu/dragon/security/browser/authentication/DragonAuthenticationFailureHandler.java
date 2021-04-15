package com.shiliu.dragon.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shiliu.dragon.untils.utils.JsonUtil;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.code.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("dragonAuthenticationFailureHandler")
public class DragonAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {
    private static final String TAG = "DragonAuthenticationFailure";
    private static Logger logger = LoggerFactory.getLogger(DragonAuthenticationFailureHandler.class);

    //springBoot开启时自动登录
    @Autowired
    private ObjectMapper objectMap;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.error("Dragon auth failed ",exception);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        if(exception instanceof UsernameNotFoundException){
            response.getWriter().write(exception.getMessage());
        }else if(exception instanceof BadCredentialsException){
            response.getWriter().write(JsonUtil.toJson(AuthResponse.USERNAME_PWD_ERROR));
        }else {
            //将authentication以json的形式输出到前端
            response.getWriter().write(JsonUtil.toJson(AuthResponse.AUTH_FAILED));
        }
    }
}
