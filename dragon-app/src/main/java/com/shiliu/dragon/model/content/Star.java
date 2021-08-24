package com.shiliu.dragon.model.content;

import com.shiliu.dragon.model.common.EventsType;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Star extends ContentEvents {

    public Star() {
        this.eventsType = EventsType.STAR;
    }

}
