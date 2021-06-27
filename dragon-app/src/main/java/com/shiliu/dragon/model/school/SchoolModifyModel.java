package com.shiliu.dragon.model.school;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class SchoolModifyModel implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(SchoolModifyModel.class);

    private static final String EQUALS = " = ";

    private String name;

    List<SchoolPair> modifyFilders = new ArrayList<SchoolPair>();

    public String model2Sql() {
        String updateSql = "";
        if (!modifyFilders.isEmpty()) {
            for (int i = 0; i < modifyFilders.size(); i++) {
                SchoolPair filder = modifyFilders.get(i);
                String name = filder.getKey().toString();
                if (StringUtils.isBlank(filder.getValue().toString())) {
                    logger.warn(name + " value is empty");

                } else {
                    updateSql += name + EQUALS + "\"" + filder.getValue().toString() + "\"" + " , ";
                }
            }
        }
        if (!updateSql.trim().isEmpty()) {
            updateSql = updateSql.substring(0, updateSql.length() - 3);
        }
        updateSql += " where name = \"" + name + "\"";
        return updateSql;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SchoolPair> getModifyFilders() {
        return modifyFilders;
    }

    public void setModifyFilders(List<SchoolPair> modifyFilders) {
        this.modifyFilders = modifyFilders;
    }

    @Override
    public String toString() {
        return "SchoolModifyModel{" +
                "name='" + name + '\'' +
                ", modifyFilders=" + modifyFilders +
                '}';
    }
}
