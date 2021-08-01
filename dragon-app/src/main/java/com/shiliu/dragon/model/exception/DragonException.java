package com.shiliu.dragon.model.exception;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class DragonException extends Exception{
    public DragonException() {
    }

    public DragonException(String add_extend_properties_error) {
        super(add_extend_properties_error);
    }
}
