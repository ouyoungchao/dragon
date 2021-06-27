package com.shiliu.dragon.security.properties;

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
	private String accessKey = "LTAI5tMBFCLKbySahs2UtXxn";

	//sk
	private String accessKeySecret = "Mc1iS2le2UymLEbaPC8hAnzId1Ob8W";

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
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSignName() {
		return signName;
	}

	public String getTemplateParam() {
		return templateParam;
	}
}
