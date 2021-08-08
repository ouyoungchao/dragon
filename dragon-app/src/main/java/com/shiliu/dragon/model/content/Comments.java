package com.shiliu.dragon.model.content;

import java.io.Serializable;
import java.util.List;

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
    private String userName;
    private String userPortrait;
    private int starts;
    private List<Comments> comments;
    private boolean isComment;

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

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public boolean isIsComment() {
        return isComment;
    }

    public void setIsComment(boolean comment) {
        isComment = comment;
    }


}
