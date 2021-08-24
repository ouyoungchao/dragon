package com.shiliu.dragon.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description 登录认证使用，承载用户认证信息
 */
public class DragonSocialUser extends SocialUser {
    private String id;
    private String userName;
    private String password;
    private String role;
    //授权机构
    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    //整数是否过期
    private boolean credentialsNonExpired;
    private boolean enabled;

    private RoleInfo roleInfo;

    public DragonSocialUser(String id, String userName, String password, String role, List<GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {

        /*SocialUser socialUser =  new SocialUser(user.getId(),password,
					true,true,true,true,
					//完成用户的授权
					AuthorityUtils.commaSeparatedStringToAuthorityList("dragon"));*/
        super(id, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public String getUserId() {
        return id;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(boolean isManager,String managerId,String token) {
        if(isManager && managerId != null) {
            this.roleInfo = new RoleInfo(isManager,managerId, token);
        }
        this.roleInfo = new RoleInfo();
    }
}

class RoleInfo implements Serializable {
    private boolean isManager;
    private String managerId;
    private String token;

    public RoleInfo() {

    }

    public RoleInfo(boolean isManager, String managerId, String token) {
        this.isManager = isManager;
        this.managerId = managerId;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }


}
