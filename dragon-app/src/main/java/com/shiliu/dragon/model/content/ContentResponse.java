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
    DELETE_SUCCESS(3005,"Delete content success"),

    QUERYCONTENT_BYID_PARAMERROR(3010,"Query param error"),
    QUERYCONTENT_BYID_SUCCESS(3011,"Query content by id success"),
    QUERYCONTENT_BYID_FAILED(3012,"Query content by id failed"),

    COMMENTS_PARAM_ERROR(3020,"Comments param error"),
    COMMENTS_SUCCESS(3021,"Comments publish success"),
    COMMENTS_FAILED(3022,"Comments failed"),
    COMMENTS_CONTENT_NOT_EXIST(3023,"Comments not exit"),
    COMMENTS_DELETE_SUCCESS(3024,"Comments delete success"),

    COMMENTS_QUERYPARAM_ERROR(3030,"Comments query param error"),
    COMMENTS_QUERYPARAM_SUCCESS(3031,"Comments query success"),
    COMMENTS_QUERYPARAM_FAIED(3032,"Comments query error"),

    CONTENT_QUERY_PARAMERROR(3040,"Query contents params error"),
    CONTENT_QUERY_FAILED(3042,"Query contents failed"),
    CONTENT_QUERY_SUCCESS(3041,"Query contents success"),

    STAR_PARAMERROR(3050,"Star params error"),
    STAR_SUCCESS(3051,"Star success"),
    STAR_CANCEL_SUCCESS(3052,"Cancel star success"),
    STAR_CONTENTID_NOTEXIST(3053,"Star content not exist"),

    COLLECT_PARAMERROR(3060,"Collect params error"),
    COLLECT_SUCCESS(3061,"Collect success"),
    COLLECT_CANCEL_SUCCESS(3062,"Cancel collect success"),
    COLLECT_CONTENTID_NOTEXIST(3063,"Collect content not exist"),

    QUERY_MYCOLLECTION_SUCCESS(3071,"Query my collections success"),
    QUERY_MYCOLLECTION_EMPTY(3072,"My collection is empty"),
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
