package com.shiliu.dragon.security.properties;

import com.shiliu.dragon.utils.mobile.MobileUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SmsCodeProperties {

	public static int EFFECTIVETIME = 3;

	/**
	 * 忘记密码，手机号认证url
	 */
	public static String AUTHMOBILE = "/dragon/authentication/mobile";

	/**
	 * 手机号登录url
	 */
	public static String LOGINMOBILE = "/dragon/login/customer";

	private int length = 6;

	//有效期3min
	private int expireIn = 60 * EFFECTIVETIME;

	//阿里云短信验证码url
	private String url= "dysmsapi.aliyuncs.com";

	//ak
	private String Ak = "TFRBSTV0NVlKdm5nU3paUWozZjluY1Ra";

	//sk
	private String Sk = "ajNwUUlrckx1TW5XMGJqSURtTTVLYmh5SktXcWY5";

	private String CHAINSIGNNAME = "太原隆玺科技";

	private String CHINATEMPLATEPARAM = "SMS_218287437";

	private String OVERSEASSIGNNAME = "太原隆玺科技海外服务";

	private String OVERSEASTEMPLATEPARAM = "SMS_219616343";
	
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

	public String getAk() {
		return new String(Base64.getDecoder().decode(Ak.getBytes(StandardCharsets.UTF_8)));
	}


	public String getSk() {
		return new String(Base64.getDecoder().decode(Sk.getBytes(StandardCharsets.UTF_8)));
	}


	public String getSignName(String tel) {
		if(MobileUtils.isChina(tel)) {
			return CHAINSIGNNAME;
		}
		return OVERSEASSIGNNAME;
	}

	public String getTemplateParam(String tel) {
		if(MobileUtils.isChina(tel)) {
			return CHINATEMPLATEPARAM;
		}
		return OVERSEASTEMPLATEPARAM;
	}
}
