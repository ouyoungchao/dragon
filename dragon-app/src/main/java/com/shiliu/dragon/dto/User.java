package com.shiliu.dragon.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.shiliu.dragon.validator.SimpleConstraint;
import com.fasterxml.jackson.annotation.JsonView;

public class User implements Serializable{

	public User() {
	}

	private String mobile;

	private String password;

	private String repassword;

	private String origin;

	private String userName;

	private String school;

	private String birthday;

	private String majorIn;

	private String smsCode;

	private String sex;

	private String name;

	private Map<String,Object> cp;

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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getCp() {
		return cp;
	}

	public void setCp(Map<String, Object> cp) {
		this.cp = cp;
	}
}
