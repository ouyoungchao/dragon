package com.shiliu.dragon.model.user;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User implements Serializable{
	private static String DEFAULT_DESCRIPTION = "这个人很懒，什么也没留下";

	public User() {
	}

	private String id;

	private String mobile;

	private String password;

	private String repassword;

	private String origin;

	private String userName;

	private String school;

	private long birthday;

	private String majorIn;

	private String smsCode;

	private byte sex;

	private String description = DEFAULT_DESCRIPTION;

	private Map<String,Object> extendProperties = new ConcurrentHashMap<>();

	public String getId() {
		if(id == null) {
			id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public String getMajorIn() {
		return majorIn;
	}

	public void setMajorIn(String majorIn) {
		this.majorIn = majorIn;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getExtendProperties() {
		return extendProperties;
	}

	public void setExtendProperties(Map<String, Object> extendProperties) {
		this.extendProperties = extendProperties;
	}

	public void addProperty(String name,String values){
		extendProperties.put(name,values);
	}

}
