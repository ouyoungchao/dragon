package com.shiliu.dragon.code;

import javax.servlet.http.HttpServletRequest;

import com.shiliu.dragon.security.validate.ImageCode;
import com.shiliu.dragon.security.validate.ValidateCodeGenerator;

//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	@Override
	public ImageCode generate(HttpServletRequest request) {
		return null;
	}
}
