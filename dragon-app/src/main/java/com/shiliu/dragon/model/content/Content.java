package com.shiliu.dragon.model.content;


import java.io.Serializable;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Content implements Serializable {

    private String id;

    private String userId;

    private String message;

    private String subject;

    /**
     * 附件
     */
    private List<String> annex;

    private long publishTime;
    //权限
    private String permissions;
    //关联帖子
    private String refrence;
    //关联人
    private List<String> refUsers;
    //点赞数
    private int starts;
    //评论数
    private int comments;

    public Content() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAnnex() {
        return annex;
    }

    public void setAnnex(List<String> annex) {
        this.annex = annex;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getRefrence() {
        return refrence;
    }

    public void setRefrence(String refrence) {
        this.refrence = refrence;
    }

    public List<String> getRefUsers() {
        return refUsers;
    }

    public void setRefUsers(List<String> refUsers) {
        this.refUsers = refUsers;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
