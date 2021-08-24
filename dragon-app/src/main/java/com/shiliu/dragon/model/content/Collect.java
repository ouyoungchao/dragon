package com.shiliu.dragon.model.content;

import com.shiliu.dragon.model.common.EventsType;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Collect extends ContentEvents {

    public Collect() {
        this.eventsType = EventsType.COLLECT;
    }

}
