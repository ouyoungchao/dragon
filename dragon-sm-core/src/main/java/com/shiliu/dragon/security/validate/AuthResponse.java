package com.shiliu.dragon.security.validate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthResponse implements Serializable {
    AUTH_SUCCESS(200,"Authen success"),
    AUTH_FAILED(400,"Authen failed"),
    USERNAME_PWD_ISEMPTY(401,"Username and pwd can not empty"),
    USERNAME_PWD_ERROR(402,"Username or pwd error"),
    USERNAME_NOT_EXIT(403,"Username not exit"),
    //η»εΊζε
    LOGOUT_SUCCESS(201,"Logout success");

    AuthResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    AuthResponse(int code, Object message, String tokenId) {
        this.code = code;
        this.message = message;
        this.tokenId = tokenId;
    }

    private int code;
    private Object message;
    private String tokenId;
    private Object roleInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Object getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(Object roleInfo) {
        this.roleInfo = roleInfo;
    }
}
