package com.shiliu.dragon.model.Audit;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditResponse implements Serializable {

    AUDIT_PARAM_ERROR("Audit param error",4000),
    UPLOADAUDITSUCCESS("Audit upload success",4001),
    UPLOADAUDITFAILED("Audit upload failed",4002),
    WAITING_AUDIT("Waiting manager audit",4003),
    AUDIT_FAILED("Audit failed",4004),
    AUDIT_SUCCESS("Audit success",4005),
    ;

    AuditResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
