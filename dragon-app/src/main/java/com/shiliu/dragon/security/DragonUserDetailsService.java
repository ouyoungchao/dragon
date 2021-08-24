package com.shiliu.dragon.security;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.security.authentication.DragonSocialUser;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.security.validate.AuthResponse;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class DragonUserDetailsService implements UserDetailsService,SocialUserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(DragonUserDetailsService.class);

	@Autowired
	private UserDao userDao;
	
	//表单登录时，根据用户名查找用户信息
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.info("登陆用户"+username);
		//new Exception("user login").printStackTrace();
		return buildUser(username);

	}

	//社交登录时，根据用户名查找用户信息
	public SocialUserDetails loadUserByUserId(String userId)
			throws UsernameNotFoundException {
		logger.info("社交登陆用户"+userId);
		return buildUser(userId);

	}

	private SocialUserDetails buildUser(String userId) {
		logger.info("Begin build user {}",userId);
		if(userId==null || userId.trim().isEmpty()){
			return null;
		}
		//根据数据库查询username的密码和授权
		//根据数据库查找到的信息判断用户是否被冻结
		//最后返回一个UserDetails的实例即可
		// TODO: 2021/3/22
		User user = userDao.queryUserByMobile(userId);
		if(user == null){
			throw new BadCredentialsException(JsonUtil.toJson(AuthResponse.USERNAME_NOT_EXIT));
		}
		String password = user.getPassword();
		//match匹配password和前台输入的密码
		try {
			/*SocialUser socialUser =  new SocialUser(user.getId(),password,
					true,true,true,true,
					//完成用户的授权
					AuthorityUtils.commaSeparatedStringToAuthorityList("dragon"));*/
			DragonSocialUser socialUser = new DragonSocialUser(user.getId(),user.getUserName(),user.getPassword(),"Dragon",AuthorityUtils.commaSeparatedStringToAuthorityList("Dragon"),true,true,true,true);
			if(user.getExtendProperties("managerId") !=null){
				socialUser.setRoleInfo(true,(String)user.getExtendProperties("managerId"),AuthUtils.encode((String)user.getExtendProperties("managerId")));
			} else{
				socialUser.setRoleInfo(false,null,null);
			}

			return socialUser;
		}catch (Exception e){
			logger.warn("BuildUser error");
			throw e;
		}

	}
}
