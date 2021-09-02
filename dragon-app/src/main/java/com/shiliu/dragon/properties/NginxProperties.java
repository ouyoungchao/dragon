package com.shiliu.dragon.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@ConfigurationProperties(prefix = "shiliu.dragon.nginx.document")
@Component
public class NginxProperties {

    private String portrait;

    private String portraitUri;
    //内容中心图片存储地址
    private String content;
    //内容中心图片访问地址
    private String contentUri;
    //审计图片上传地址
    private String audit;
    //审计图片访问uri
    private String auditUri;


    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getAuditUri() {
        return auditUri;
    }

    public void setAuditUri(String auditUri) {
        this.auditUri = auditUri;
    }
}
