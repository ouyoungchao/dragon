package com.shiliu.dragon.security.validate;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ValidateCode implements Serializable {

	private String code;
	private LocalDateTime expireTime;
	
	public ValidateCode(String code,int expireIn){
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}
	
	public ValidateCode(String code,LocalDateTime expireTime){
		this.code = code;
		this.expireTime = expireTime;
	}

	public boolean isExpired(){
		return LocalDateTime.now().isAfter(expireTime);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}
}
