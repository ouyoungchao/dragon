package com.shiliu.dragon.untils;

import com.shiliu.dragon.untils.cache.SessionCache;
import com.shiliu.dragon.controller.UserResponse;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.security.validate.code.SmsResponse;
import com.shiliu.dragon.security.validate.code.ValidateCode;
import com.shiliu.dragon.security.validate.code.ValidateCodeException;
import com.shiliu.dragon.untils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;

/**
 * @author ouyangchao
 * @createTime
 * @description 用户校验器
 */
public class UserInspector {
    private static Logger logger = LoggerFactory.getLogger(UserInspector.class);

    /**
     * 校验sm是否合法
     *
     * @param user
     * @return
     * @throws ServletRequestBindingException
     * @throws ValidateCodeException
     */
    public static boolean validUser(User user) throws ServletRequestBindingException, ValidateCodeException {
        //请求参数值
        if (user == null) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(SmsResponse.SMSISEMPTY));
        }
        try {
            isValidMobile(user.getMobile());
            isValidSMS(user);
            isValidPwd(user);
            isvalidName(user.getUserName());
        } catch (ValidateCodeException e) {
            logger.warn("invalidUser");
            throw e;
        }
        return true;
    }

    public static boolean isvalidName(String userName) {
        if (StringUtils.isBlank(userName)) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.USERNAME_ISEMPTY));
        }
        return true;
    }

    /**
     * 校验密码是否合法
     *
     * @param user
     * @return
     * @throws ValidateCodeException
     */
    public static boolean isValidPwd(User user) throws ValidateCodeException {
		/*if(!REGEX_PASSWORD_STRONG.matches(user.getPassword())){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
		}*/
        if (StringUtils.trim(user.getPassword()).length() < 8) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
        }
        if (!user.getPassword().equals(user.getRepassword())) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.PASSWORD_REPEAT));
        }
        return true;
    }

    public static boolean isValidPwd(String pwd) throws ValidateCodeException {
		/*if(!REGEX_PASSWORD_STRONG.matches(user.getPassword())){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
		}*/
        if (pwd ==null || StringUtils.trim(pwd).length() < 8) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
        }
        return true;
    }

    /**
     * 校验电话号码
     *
     * @param mobile
     * @return
     * @throws ValidateCodeException
     */
    public static boolean isValidMobile(String mobile) throws ValidateCodeException {
        if (mobile != null && mobile.length() == 13) {
            return true;
        }
        throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.INVALIDMOBILE));
    }

    public static boolean isValidUserId(String userId) throws ValidateCodeException {
        if (userId != null && userId.length() == 32) {
            return true;
        }
        throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(UserResponse.INVALIDPARAM));
    }

    /**
     * 校验短信验证码
     *
     * @param user
     * @return
     * @throws ValidateCodeException
     */
    public static boolean isValidSMS(User user) throws ValidateCodeException {
        String smsInRequest = user.getSmsCode();
        if (StringUtils.isBlank(smsInRequest)) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(SmsResponse.SMSISEMPTY));
        }
        //系统生成值
        Object cachedObject = SessionCache.getValueFromCache(user.getMobile());
        //验证码不存在
        if (cachedObject == null) {
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(SmsResponse.SMSNOTEXIST));
        }
        ValidateCode codeInSession = (ValidateCode) cachedObject;
        //验证码已经过期
        if (codeInSession.isExpired()) {
            SessionCache.removeFromCache(user.getMobile());
            throw new ValidateCodeException(com.shiliu.dragon.untils.utils.JsonUtil.toJson(SmsResponse.SMSEXPIRED));
        }
        //验证码不匹配
        if (!StringUtils.equals(codeInSession.getCode(), smsInRequest)) {
            throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSUNCORRECT));
        }
        return true;
    }
}
