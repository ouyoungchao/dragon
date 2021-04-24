package com.shiliu.dragon.model.user;

import com.shiliu.dragon.model.Pair;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserPair implements Pair<String,Object> {
    private String key;
    private Object value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
