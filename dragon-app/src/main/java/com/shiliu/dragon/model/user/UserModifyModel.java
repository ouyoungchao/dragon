package com.shiliu.dragon.model.user;

import com.shiliu.dragon.utils.UserInspector;
import com.shiliu.dragon.utils.utils.JsonUtil;
import com.shiliu.dragon.security.validate.code.ValidateCodeException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserModifyModel implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(UserModifyModel.class);

    private static final String EQUALS = " = ";

    private String id;

    List<UserPair> modifyFilders = new ArrayList<UserPair>();

    public boolean isValidFilders(PasswordEncoder passwordEncoder) throws ValidateCodeException {
        if (!modifyFilders.isEmpty()) {
            for (int i = 0; i < modifyFilders.size(); i++) {
                UserPair filder = modifyFilders.get(i);
                if (filder.getKey().toString().equalsIgnoreCase("mobile")) {
                    UserInspector.isValidMobile(filder.getValue().toString());
                }
                if (filder.getKey().toString().equalsIgnoreCase("password")) {
                    UserInspector.isValidPwd(filder.getValue().toString());
                    filder.setValue(passwordEncoder.encode(filder.getValue().toString()));
                }
                if (filder.getKey().toString().equalsIgnoreCase("userName")) {
                    UserInspector.isvalidName(filder.getValue().toString());
                }
                if(filder.getKey().toString().equalsIgnoreCase("sex")){
                    UserInspector.isValidSex((int)filder.getValue());
                }

            }
            return true;
        }
        throw new ValidateCodeException(JsonUtil.toJson(UserResponse.EMPTY_FILDERS));
    }

    public String model2Sql() {
        String updateSql = "";
        if (!modifyFilders.isEmpty()) {
            for (int i = 0; i < modifyFilders.size(); i++) {
                UserPair filder = modifyFilders.get(i);
                String name = filder.getKey().toString();
                if (StringUtils.isBlank(filder.getValue().toString())) {
                    logger.warn(name + " value is empty");

                } else if (name.equalsIgnoreCase("sex")) {
                    updateSql += name + EQUALS + (filder.getValue()) + " , ";
                } else {
                    updateSql += name + EQUALS + "\"" + filder.getValue().toString() + "\"" + " , ";
                }
            }
        }
        if (!updateSql.trim().isEmpty()) {
            updateSql = updateSql.substring(0, updateSql.length() - 3);
        }
        updateSql += " where id = \"" + id + "\"";
        return updateSql;
    }

    @Override
    public String toString() {
        return "UserModifyModel{" +
                "id='" + id + '\'' +
                ", modifyFilders=" + modifyFilders +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserPair> getModifyFilders() {
        return modifyFilders;
    }

    public void setModifyFilders(List<UserPair> modifyFilders) {
        this.modifyFilders = modifyFilders;
    }
}
