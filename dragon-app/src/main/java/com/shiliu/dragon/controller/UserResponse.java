package com.shiliu.dragon.controller;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * 用户注册，修改相关响应信息
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserResponse implements Serializable {
    INVALIDPARAM(2000,"Invalid param"),
    INVALIDMOBILE(2001,"Invalid mobile"),
    PASSWORD_REPEAT(2002,"Passwords are diff"),
    PASSWORD_RULE_NOTSATISFIED(2003,"Password rules not satisfied"),
    USERNAME_ISEMPTY(2004,"Username invalid"),
    SCHOOL_MAJOR_ORIGIN_EMPTY(2005,"Scholl、major or origin can not empty"),
    SEX_ERROR(2007,"Sex must be man、woman or not set"),
    MOBILE_REGISTED(2006,"Mobile has registed"),
    REGISTER_SUCCESS(2008,"Register success"),
    Modify_SUCCESS(2031,"Modify user success"),
    UPDATE_PWD(2032,"Update pwd success"),
    EMPTY_FILDERS(2030,"Modify filders is empty"),
    USER_NOT_EXIST(2021,"User not exist"),
    QUERY_USER_SUCCESS(2022,"QueyUser success"),
    //图片上传成功
    UPLOAD_PORTRAIT_SUCCESS(2040,"Upload portrait success"),
    MODIFY_PORTRAIT_SUCCESS(2041,"Modify portrait success");

    UserResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private Object message;

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
}
