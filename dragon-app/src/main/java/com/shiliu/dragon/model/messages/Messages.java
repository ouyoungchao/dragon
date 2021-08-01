package com.shiliu.dragon.model.messages;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Messages implements Comparable {
    /**
     * 消息id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 评论，点赞者的昵称
     */
    private String relatedUserName;

    /**
     * 评论，点赞者的头像
     */
    private String relatedUserPortrait;

    /**
     * 评论，点赞者的id
     */
    private String relatedUserId;

    /**
     * 评论，点赞者的内容
     */
    private String content;

    private String contentId;

    /**
     * 消息产生时间
     */
    private long productedTime;

    /**
     * 消息类型
     */
    private MessageTypes messageType;

    public Messages() {
    }

    public Messages(String id, String userId, String relatedUserName, String relatedUserPortrait, String relatedUserId, String content, String contentId, long productedTime, MessageTypes messageType) {
        this.id = id;
        this.userId = userId;
        this.relatedUserName = relatedUserName;
        this.relatedUserPortrait = relatedUserPortrait;
        this.relatedUserId = relatedUserId;
        this.content = content;
        this.contentId = contentId;
        this.productedTime = productedTime;
        this.messageType = messageType;
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

    public String getRelatedUserName() {
        return relatedUserName;
    }

    public void setRelatedUserName(String relatedUserName) {
        this.relatedUserName = relatedUserName;
    }

    public String getRelatedUserPortrait() {
        return relatedUserPortrait;
    }

    public void setRelatedUserPortrait(String relatedUserPortrait) {
        this.relatedUserPortrait = relatedUserPortrait;
    }

    public String getRelatedUserId() {
        return relatedUserId;
    }

    public void setRelatedUserId(String relatedUserId) {
        this.relatedUserId = relatedUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getProductedTime() {
        return productedTime;
    }

    public void setProductedTime(long productedTime) {
        this.productedTime = productedTime;
    }

    public MessageTypes getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypes messageType) {
        this.messageType = messageType;
    }

    public int compareTo(Object o) {
        if (this.productedTime < ((Messages)o).getProductedTime()) {
            return 1;
        } else if (this.productedTime > ((Messages)o).getProductedTime()) {
            return -1;
        }
        return 0;
    }
}
