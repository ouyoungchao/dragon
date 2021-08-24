package com.shiliu.dragon.model.Audit;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class Audits {

    private String id;
    private String userId;
    private String meterials;
    private AuditStatus status;
    private long postData;
    private long auditData;
    private String managerId;
    private boolean isManager;
    private String school;

    public Audits() {
    }

    public Audits(String userId, String meterials, AuditStatus status, long postData, long auditData, String managerId, boolean isManager, String school) {
        this.userId = userId;
        this.meterials = meterials;
        this.status = status;
        this.postData = postData;
        this.auditData = auditData;
        this.managerId = managerId;
        this.isManager = isManager;
        this.school = school;
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

    public String getMeterials() {
        return meterials;
    }

    public void setMeterials(String meterials) {
        this.meterials = meterials;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public long getPostData() {
        return postData;
    }

    public void setPostData(long postData) {
        this.postData = postData;
    }

    public long getAuditData() {
        return auditData;
    }

    public void setAuditData(long auditData) {
        this.auditData = auditData;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Audits{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", meterials='" + meterials + '\'' +
                ", status=" + status +
                ", postData=" + postData +
                ", auditData=" + auditData +
                ", managerId='" + managerId + '\'' +
                ", isManager=" + isManager +
                '}';
    }
}
