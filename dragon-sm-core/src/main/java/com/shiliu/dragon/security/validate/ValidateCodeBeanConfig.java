package com.shiliu.dragon.security.validate;

import com.shiliu.dragon.security.validate.sms.AliSmsCodeSender;
import com.shiliu.dragon.security.validate.sms.DefaultSmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shiliu.dragon.security.properties.SecurityProperties;

@Configuration
public class ValidateCodeBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	@ConditionalOnMissingBean(name = "imageCodeGenerator")
	public ValidateCodeGenerator imageCodeGenerator(){
		ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
		imageCodeGenerator.setSecurityProperties(securityProperties);
		return imageCodeGenerator;
	}
	
	@Bean
	@ConditionalOnMissingBean(name = "smsCodeSender")
	public DefaultSmsCodeSender smsCodeSender(){
		return new AliSmsCodeSender();
	}
}
