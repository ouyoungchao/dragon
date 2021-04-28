package com.shiliu.dragon.model.Audit;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public enum AuditResponse {

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



}
