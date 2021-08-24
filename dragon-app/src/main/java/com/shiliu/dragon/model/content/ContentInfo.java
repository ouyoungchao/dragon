package com.shiliu.dragon.model.content;

import com.shiliu.dragon.model.user.User;

import java.io.Serializable;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description 帖子相关信息，包含帖子内容，发帖人信息，评论信息等
 */
public class ContentInfo implements Serializable {

    private Content content;

    private User user;

    private List<Comments> comments;

    private List<ContentEvents> stars;

    public ContentInfo(Content content, User user, List<Comments> comments,List<ContentEvents> stars) {
        this.content = content;
        this.user = user;
        this.comments = comments;
        this.stars = stars;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ContentEvents> getStars() {
        return stars;
    }

    public void setStars(List<ContentEvents> stars) {
        this.stars = stars;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
