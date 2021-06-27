package com.shiliu.dragon.security.properties;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SmsCodeProperties {

	public static int EFFECTIVETIME = 3;

	/**
	 * 手机号登陆url
	 */
	public static String AUTHMOBILE = "/dragon/authentication/mobile";

	/**
	 * 注册用户url
	 */
	public static String USERREGISTER = "/dragon/user/register";

	private int length = 6;

	//有效期3min
	private int expireIn = 60 * EFFECTIVETIME;

	//阿里云短信验证码url
	private String url= "dysmsapi.aliyuncs.com";

	//ak
	private String accessKey = "TFRBSTV0TUJGQ0xLYnlTYWhzMlV0WHhu";

	//sk
	private String accessKeySecret = "TWMxaVMybGUyVXltTEViYVBDOGhBbnpJZDFPYjhX";

	private String signName = "太原隆玺科技";

	private String templateParam = "SMS_218287437";
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessKey() {
		return new String(Base64.getDecoder().decode(accessKey.getBytes(StandardCharsets.UTF_8)));
	}


	public String getAccessKeySecret() {
		return new String(Base64.getDecoder().decode(accessKeySecret.getBytes(StandardCharsets.UTF_8)));
	}


	public String getSignName() {
		return signName;
	}

	public String getTemplateParam() {
		return templateParam;
	}
}
