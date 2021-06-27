package com.shiliu.dragon.security.validate.sms;

public interface SmsCodeSender {
	boolean sendSmsCode(String mobile,String code);
}
