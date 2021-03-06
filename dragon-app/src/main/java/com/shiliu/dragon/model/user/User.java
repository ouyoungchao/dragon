package com.shiliu.dragon.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User implements Serializable {


    public static String DEFAULT_DESCRIPTION = "这个人很懒，什么也没留下";
    public static String PORTRAITURI_NAME = "portraitUri";
    //默认头像
    public static String PORTRAITURI_DEFAULT_VALUE = "defaultPortrait.png";

    public User() {
    }

    public User(String mobile, String password, String repassword, String origin, String userName, String school, String majorIn, byte sex) {
        this.mobile = mobile;
        this.password = password;
        this.repassword = repassword;
        this.origin = origin;
        this.userName = userName;
        this.school = school;
        this.majorIn = majorIn;
        this.sex = sex;
    }

    private String id;

    private String mobile;

    private String password;

    private transient String repassword;

    private String origin;

    private String userName;

    private String school;

    private long birthday;

    private String majorIn;

    private transient String smsCode;

    private byte sex;

    private long registerTime;

    private String description = DEFAULT_DESCRIPTION;

    private Map<String, Object> extendProperties = new ConcurrentHashMap<>();

    public Object getExtendProperties(String name) {
        if (name == null) {
            return null;
        }
        return extendProperties.get(name);
    }

    public String getId() {
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


    public String getPortrait() {
        return (String) extendProperties.get(PORTRAITURI_NAME);
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

    public void addProperty(String name, Object values) {
        extendProperties.put(name, values);
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }
}
