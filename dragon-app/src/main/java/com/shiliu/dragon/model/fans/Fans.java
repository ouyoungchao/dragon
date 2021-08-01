package com.shiliu.dragon.model.fans;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Fans {
    private String id;
    private String uper;
    private String follow;
    private long watchTimer;

    public Fans(String id, String uper, String follow, long watchTimer) {
        this.id = id;
        this.uper = uper;
        this.follow = follow;
        this.watchTimer = watchTimer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUper() {
        return uper;
    }

    public void setUper(String uper) {
        this.uper = uper;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public long getWatchTimer() {
        return watchTimer;
    }

    public void setWatchTimer(long watchTimer) {
        this.watchTimer = watchTimer;
    }

    @Override
    public String toString() {
        return "Fans{" +
                ", uper='" + uper + '\'' +
                ", follow='" + follow + '\'' +
                '}';
    }
}
