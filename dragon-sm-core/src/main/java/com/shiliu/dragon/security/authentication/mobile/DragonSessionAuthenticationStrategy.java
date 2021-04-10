package com.shiliu.dragon.security.authentication.mobile;

import com.shiliu.dragon.common.cache.SessionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.social.security.SocialUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DragonSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws SessionAuthenticationException {
        logger.debug("add seeion " + authentication);
        SocialUser socialUser = (SocialUser) authentication.getPrincipal();
        SessionCache.addSession(socialUser.getUserId(),authentication);
    }
}
