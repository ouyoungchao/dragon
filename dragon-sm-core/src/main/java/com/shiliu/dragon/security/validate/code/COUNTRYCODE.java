package com.shiliu.dragon.security.validate.code;

public enum COUNTRYCODE {

    /**
     * 中国
     */
    CHINA("86");

    private String code;


    COUNTRYCODE(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
