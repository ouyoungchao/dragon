package com.shiliu.dragon.model.fans;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FansResponse {
    FANS_PARAM_ERROR(6000,"Invalid param"),
    FANS_FOLLOW_SUCCESS(6001,"Follow success"),
    FANS_CANCEL_FOLLOW_SUCCESS(6002,"Cancel follow success"),
    FANS_USER_NOTEXIT(6003,"User not exit"),
    FANS_QUERY_SUCCESS(6011,"Query fans success")
    ;

    int code;
    Object message;

    FansResponse(int code, Object message) {
        this.code = code;
        this.message = message;
    }

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
