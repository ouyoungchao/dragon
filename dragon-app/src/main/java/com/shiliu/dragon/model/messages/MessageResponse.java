package com.shiliu.dragon.model.messages;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageResponse {
    QUERYMESSAGESUCCESS(7001,"Query message success"),
    NOMESSAGES(7002,"No messages")
    ;

    MessageResponse(int code, Object message) {
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
