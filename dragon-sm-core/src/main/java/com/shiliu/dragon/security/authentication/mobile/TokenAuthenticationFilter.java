package com.shiliu.dragon.security.authentication.mobile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String FINISHEDAUTHENTICATION = "finishedAuthentication";

	private static String URL = "/dragon";

	private static String TOKEN = "token";

	private AntPathMatcher pathMatch = new AntPathMatcher();

	protected TokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	protected TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	public TokenAuthenticationFilter() {
		super(URL);
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("TokenAuthenticationFilter doFilter");
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		if (!this.requiresAuthentication(request, response)) {
			chain.doFilter(request, response);
		}else {

				String token = req.getParameter(TOKEN);
				if(isValidToken(token)){
					chain.doFilter(request, response);
				}else{
					System.out.print("token is null");
					this.unsuccessfulAuthentication(request, response, new AuthenticationException("Token is invalid") {
						@Override
						public synchronized Throwable getCause() {
							return super.getCause();
						}
					});
				}

		}

//		new Exception("Token").printStackTrace();
	}

	private boolean requireAuthentication(HttpServletRequest req){
		if(pathMatch.match(URL, req.getRequestURI())){
			return true;
		}
		return false;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		System.out.println("TokenAuthenticationFilter attemptAuthentication");
		return null;
	}

	private boolean isValidToken(String token){
		if(token == null || token.trim().isEmpty()){
			return false;
		}else{
			return true;
		}
	}

}

