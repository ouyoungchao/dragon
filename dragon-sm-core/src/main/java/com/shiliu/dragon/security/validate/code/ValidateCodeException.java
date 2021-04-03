package com.shiliu.dragon.security.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 短信验证码异常处理逻辑
 */
public class ValidateCodeException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public ValidateCodeException (String msg){
		super(msg);
	}
}
