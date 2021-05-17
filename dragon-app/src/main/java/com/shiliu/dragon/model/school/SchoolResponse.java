package com.shiliu.dragon.model.school;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SchoolResponse {
    SCHOOL_PARAM_ERROR(5000,"School param error"),
    SCHOOL_ADD_SUCCESS(5001,"Add school success"),
    SCHOOL_ADD_FAILED(5002,"Add school Failed"),
    SCHOOL_ADD_EXIST(5003,"School has existed"),

    QUERYSCHOOL_BYNAME_PARAMERROR(5010,"Query param error"),
    QUERYSCHOOL_BYNAME_SUCCESS(5011,"Query school by name success"),
    QUERYSCHOOL_BYNAME_FAILED(5012,"Query school by name failed"),

    QUERYSCHOOL_PARAMERROR(5020,"Query param error"),
    QUERYSCHOOL_SUCCESS(5021,"Query school success"),
    QUERYSCHOOL_FAILED(5022,"Query school failed"),
    ;

    SchoolResponse(int code, Object message) {
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
