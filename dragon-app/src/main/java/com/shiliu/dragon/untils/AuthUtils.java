package com.shiliu.dragon.untils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class AuthUtils {

    public static String getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        //适配ios的header无法传token问题
        if (token == null) {
            token = request.getParameter("token");
        }
        if (token != null) {
            return new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        }
        return null;
    }
}
