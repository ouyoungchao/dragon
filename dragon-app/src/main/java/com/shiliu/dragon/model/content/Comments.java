package com.shiliu.dragon.model.content;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description 评论模板
 */
public class Comments implements Serializable {

    private String id;
    private String contentId;
    private String userId;
    private String message;
    private long commmentTime;
    private int starts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCommmentTime() {
        return commmentTime;
    }

    public void setCommmentTime(long commmentTime) {
        this.commmentTime = commmentTime;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
