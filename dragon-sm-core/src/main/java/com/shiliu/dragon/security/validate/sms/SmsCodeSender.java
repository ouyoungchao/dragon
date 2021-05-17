package com.shiliu.dragon.security.validate.sms;

public interface SmsCodeSender {
	void sendSmsCode(String mobile,String code);
}
