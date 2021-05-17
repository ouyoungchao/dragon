package com.shiliu.dragon.model.school;

import java.io.Serializable;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class School implements Serializable {
    private String id;
    private String name;
    private String description;
    private String url;
    private List<String> annex;

    public School() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getAnnex() {
        return annex;
    }

    public void setAnnex(List<String> annex) {
        this.annex = annex;
    }
}
