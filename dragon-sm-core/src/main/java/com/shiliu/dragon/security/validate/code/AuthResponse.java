package com.shiliu.dragon.security.validate.code;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthResponse implements Serializable {
    AUTH_SUCCESS(200,"authen success"),
    AUTH_FAILED(400,"authen failed");

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
}
