package com.shiliu.dragon.security.authentication.mobile;

import com.shiliu.dragon.common.cache.SessionCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.security.SocialUser;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DragonSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws SessionAuthenticationException {
        System.out.println("add seeion " + authentication);
        SocialUser socialUser = (SocialUser) authentication.getPrincipal();
        SessionCache.addSession(socialUser.getUserId(),authentication);
    }
}
