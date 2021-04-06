package com.shiliu.dragon.security.validate.code;

import org.springframework.web.context.request.RequestAttributes;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class MobileRequest implements RequestAttributes {
    @Override
    public Object getAttribute(String s, int i) {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o, int i) {

    }

    @Override
    public void removeAttribute(String s, int i) {

    }

    @Override
    public String[] getAttributeNames(int i) {
        return new String[0];
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable, int i) {

    }

    @Override
    public Object resolveReference(String s) {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Object getSessionMutex() {
        return null;
    }
}
