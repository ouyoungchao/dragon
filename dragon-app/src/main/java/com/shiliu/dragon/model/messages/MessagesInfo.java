package com.shiliu.dragon.model.messages;

import java.io.Serializable;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class MessagesInfo implements Serializable {
    private List<Messages> comments;
    private List<Messages> stars;
    private List<Messages> followers;
    private List<Messages> collects;

    public MessagesInfo(List<Messages> comments, List<Messages> stars, List<Messages> followers,List<Messages> collects) {
        this.comments = comments;
        this.stars = stars;
        this.followers = followers;
        this.collects = collects;

    }

    public List<Messages> getComments() {
        return comments;
    }

    public void setComments(List<Messages> comments) {
        this.comments = comments;
    }

    public List<Messages> getStars() {
        return stars;
    }

    public void setStars(List<Messages> stars) {
        this.stars = stars;
    }

    public List<Messages> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Messages> followers) {
        this.followers = followers;
    }
}
