package com.shiliu.dragon.security.validate.code;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeGenerator {
	ValidateCode generate(HttpServletRequest request);
}
