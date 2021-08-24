package com.shiliu.dragon.model.content;

import com.shiliu.dragon.model.common.EventsType;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class ContentEvents implements Serializable {
    protected String id;
    protected String userId;
    protected String contentId;
    protected long accurrentTime;
    protected String userName;
    protected String userPortrait;
    protected EventsType eventsType;

    public ContentEvents() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getAccurrentTime() {
        return accurrentTime;
    }

    public void setAccurrentTime(long accurrentTime) {
        this.accurrentTime = accurrentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public EventsType getEventsType() {
        return eventsType;
    }

    public void setEventsType(EventsType eventsType) {
        this.eventsType = eventsType;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }
}
