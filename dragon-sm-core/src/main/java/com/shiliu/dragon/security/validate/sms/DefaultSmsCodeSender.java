package com.shiliu.dragon.security.validate.sms;

import com.shiliu.dragon.security.properties.SmsCodeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSmsCodeSender implements SmsCodeSender {
	private Logger logger = LoggerFactory.getLogger(getClass());

	protected static SmsCodeProperties smsCodeProperties = new SmsCodeProperties();

	public void sendSmsCode(String mobile, String code) {
		logger.info("Send {} to mobile {}",code,mobile);
	}
}
