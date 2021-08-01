package com.shiliu.dragon.model.user;

import com.shiliu.dragon.model.fans.Fans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description 用户相关的信息
 */
public class UserInfo implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(UserInfo.class);

    private User user;
    private List<Fans> upper;
    private List<Fans> fans;

    public UserInfo(User user, List<Fans> upper, List<Fans> fans) {
        this.user = user;
        this.upper = upper;
        this.fans = fans;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Fans> getUpper() {
        return upper;
    }

    public void setUpper(List<Fans> upper) {
        this.upper = upper;
    }

    public List<Fans> getFans() {
        return fans;
    }

    public void setFans(List<Fans> fans) {
        this.fans = fans;
    }
}
