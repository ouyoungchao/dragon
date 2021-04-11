package com.shiliu.dragon.model.user;

import com.shiliu.dragon.common.UserInspector;
import com.shiliu.dragon.common.utils.JsonUtil;
import com.shiliu.dragon.controller.UserResponse;
import com.shiliu.dragon.security.validate.code.ValidateCodeException;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserModifyModel implements Serializable {
    private static final String EQUALS = " = ";

    private String id;

    List<Pair> modifyFilders = new ArrayList<Pair>();

   public boolean isValidFilders() throws ValidateCodeException {
       if(!modifyFilders.isEmpty()){
           for (int i=0; i<modifyFilders.size(); i++){
               Pair filder = modifyFilders.get(i);
               if(filder.getFirst().toString().equalsIgnoreCase("mobile")){
                   UserInspector.isValidMobile(filder.getSecond().toString());
               }
               if(filder.getFirst().toString().equalsIgnoreCase("password")){
                   UserInspector.isValidPwd(filder.getSecond().toString());
               }
               if (filder.getFirst().toString().equalsIgnoreCase("userName")){
                   UserInspector.isvalidName(filder.getSecond().toString());
               }
           }
           return true;
       }
       throw new ValidateCodeException(JsonUtil.toJson(UserResponse.EMPTY_FILDERS));
   }

    public String model2Sql(){
        String updateSql = "";
        if(!modifyFilders.isEmpty()){
            for (int i=0; i<modifyFilders.size(); i++){
                Pair filder = modifyFilders.get(i);
                String name = filder.getFirst().toString();
                if(name.equalsIgnoreCase("sex")){
                    updateSql += name + EQUALS + ((byte)filder.getSecond()) + " , ";
                }else {
                    updateSql += name + EQUALS + "\"" + filder.getSecond().toString() + "\"" + " , ";
                }
            }
        }
        if(!updateSql.trim().isEmpty()){
            updateSql = updateSql.substring(0,updateSql.length()-3);
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

    public List<Pair> getModifyFilders() {
        return modifyFilders;
    }

    public void setModifyFilders(List<Pair> modifyFilders) {
        this.modifyFilders = modifyFilders;
    }
}
