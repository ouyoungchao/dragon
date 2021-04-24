package com.shiliu.dragon.untils;

import org.apache.commons.lang.StringUtils;

/**
 * @author ouyangchao
 * @createTime
 * @description 内容校验器
 */
public class ContentInspector {
    private static final int MESSAGE_LENGTH = 1024;

    public static boolean isValidMessage(String message){
        if(StringUtils.isBlank(message) || message.trim().length() > MESSAGE_LENGTH){
            return false;
        }
        return true;
    }
}
