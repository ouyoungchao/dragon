package com.shiliu.dragon.security.authentication.mobile;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DragonSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    String SESSION_ID = "session";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws SessionAuthenticationException {
        System.out.println("add seeion " + authentication);
        sessionStrategy.setAttribute(new ServletWebRequest(httpServletRequest),SESSION_ID,authentication);
    }
}
