package com.shiliu.dragon.model.log;

import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Operations implements Serializable {
    private String id;

    private String userId;

    private String uri;

    private String remoteAddress;

    private int status;

    private long startTime;

    private long endTime;

    public Operations() {
    }

    public Operations(String id, String userId, String uri, String remoteAddress, int status, long startTime, long endTime) {
        this.id = id;
        this.userId = userId;
        this.uri = uri;
        this.remoteAddress = remoteAddress;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
