package com.shiliu.dragon.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shiliu.dragon.common.utils.JsonUtil;
import com.shiliu.dragon.security.browser.support.SimpleResponse;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.code.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrowserSecurityController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/*@RequestMapping("/login")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse require(HttpServletRequest request, HttpServletResponse response) throws IOException{

		return new SimpleResponse(JsonUtil.toJson(AuthResponse.AUTH_FAILED));
	}*/
}
