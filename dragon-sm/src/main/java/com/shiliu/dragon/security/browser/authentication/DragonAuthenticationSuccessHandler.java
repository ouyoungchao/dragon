package com.shiliu.dragon.security.browser.authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shiliu.dragon.utils.utils.JsonUtil;
import com.shiliu.dragon.security.validate.code.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.social.security.SocialUser;
import org.springframework.stereotype.Component;

@Component("dragonAuthenticationSuccessHandler")
public class DragonAuthenticationSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        logger.info("登录成功，开始授权");
        if (response !=null) {
            //如果为json格式，则返回json数据，如果不是则跳转
            response.setContentType("application/json;charset=UTF-8");
            //将authentication以json的形式输出到前端
            AuthResponse authResponse = AuthResponse.AUTH_SUCCESS;
            authResponse.setMessage(authentication);
            authResponse.setTokenId(generateToken(authentication));
            response.getWriter().write(JsonUtil.toJson(authResponse));
        }
        }

        private String generateToken(Authentication authentication){
            SocialUser socialUser = (SocialUser) authentication.getPrincipal();
            String id = socialUser.getUserId();
            return new String(Base64.getEncoder().encode(id.getBytes(StandardCharsets.UTF_8)));

        }
}
