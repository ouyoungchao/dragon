package com.shiliu.dragon.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.StringUtils;
import com.shiliu.dragon.security.browser.support.SimpleResponse;
import com.shiliu.dragon.security.properties.LoginType;
import com.shiliu.dragon.security.properties.SecurityProperties;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("dragonAuthenticationFailureHandler")
public class DragonAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {
    private static final String TAG = "DragonAuthenticationFailure";
    private Logger logger = LoggerFactory.getLogger(getClass());

    //springBoot开启时自动登录
    @Autowired
    private ObjectMapper objectMap;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.error(TAG, "authentication failed ", exception);
//		exception.printStackTrace();
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            //将authentication以json的形式输出到前端
            if (!StringUtils.isEmptyOrWhitespaceOnly(exception.getMessage())) {
                response.getWriter().write(objectMap.writeValueAsString(new SimpleResponse(exception.getMessage())));
            }
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
