package com.shiliu.dragon.security.validate.code.sms;

import com.shiliu.dragon.security.properties.SmsCodeProperties;

public class DefaultSmsCodeSender implements SmsCodeSender {

	protected static SmsCodeProperties smsCodeProperties = new SmsCodeProperties();

	public void sendSmsCode(String mobile, String code) {
		System.out.println("向手机"+mobile+"发送验证码"+code);
	}
}
