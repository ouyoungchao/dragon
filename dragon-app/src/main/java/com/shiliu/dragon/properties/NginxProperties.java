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

    private String ortrait;

    private String uri;

    public String getOrtrait() {
        return ortrait;
    }

    public void setOrtrait(String ortrait) {
        this.ortrait = ortrait;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
