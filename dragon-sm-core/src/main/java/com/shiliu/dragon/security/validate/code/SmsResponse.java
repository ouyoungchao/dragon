package com.shiliu.dragon.security.validate.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.properties.SmsCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SmsResponse implements Serializable{

    /**
     * 参数不合法
     */
    INVALIDPARAM(1001,"The param is invalid"),

    /**
     * 当前国家不支持
     */
    UNSUPPORTCOUNTRY(1002,"The country not support"),

    /**
     * 验证码发送成功
     */
    SUCCESS(1000,"Send sms success", SmsCodeProperties.EFFECTIVETIME),

    SMSISEMPTY(1003,"Sms is empty"),

    SMSNOTEXIST(1004,"Sms not exist"),

    SMSEXPIRED(1005,"Sms is Expired"),

    SMSUNCORRECT(1006,"Sms is uncorrect");



    @Autowired
    private SecurityProperties securityProperties;

    private int code;
    private String message;
    private int effectiveTime;

    SmsResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    SmsResponse(int code, String message, int effectiveTime) {
        this.code = code;
        this.message = message;
        this.effectiveTime = effectiveTime;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
