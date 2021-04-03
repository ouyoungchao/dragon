package com.shiliu.dragon.security.validate.code.sms;

public interface SmsCodeSender {
	void sendSmsCode(String mobile,String code);
}
