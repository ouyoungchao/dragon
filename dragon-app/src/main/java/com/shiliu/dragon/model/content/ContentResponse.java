package com.shiliu.dragon.model.content;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContentResponse {
    CONTENT_PARAM_ERROR(3000,"Content param error"),
    PUBLISH_SUCCESS(3001,"Publish content success"),
    PUBLISH_FAILED(3002,"Publish Content Failed"),
    CONTENT_INVALID(3003,"Not meet legal requirement"),
    MESSAGE_INVALID(3004,"Message is invalid"),

    QUERYCONTENT_BYID_PARAMERROR(3010,"Query param error"),
    QUERYCONTENT_BYID_SUCCESS(3011,"Query content by id success"),
    QUERYCONTENT_BYID_FAILED(3012,"Query content by id failed"),

    COMMENTS_PARAM_ERROR(3020,"Comments param error"),
    COMMENTS_SUCCESS(3021,"Comments publish success"),
    COMMENTS_FAILED(3022,"Comments failed"),

    COMMENTS_QUERYPARAM_ERROR(3030,"Comments query param error"),
    COMMENTS_QUERYPARAM_SUCCESS(3031,"Comments query success"),
    COMMENTS_QUERYPARAM_FAIED(3032,"Comments query error"),

    CONTENT_QUERY_PARAMERROR(3040,"Query contents params error"),
    CONTENT_QUERY_FAILED(3042,"Query contents failed"),
    CONTENT_QUERY_SUCCESS(3041,"Query contents success"),

    ;

    ContentResponse(int code, String message) {
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
