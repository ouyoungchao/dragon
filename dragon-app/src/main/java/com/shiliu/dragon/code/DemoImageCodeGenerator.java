package com.shiliu.dragon.code;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.shiliu.dragon.security.validate.code.ImageCode;
import com.shiliu.dragon.security.validate.code.ValidateCodeGenerator;

//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	@Override
	public ImageCode generate(HttpServletRequest request) {
		return null;
	}
}
