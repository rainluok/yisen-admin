package com.yisen.core.context;

import com.yisen.module.system.user.model.vo.LoginUserVO;

/**
 * @author rainluo
 * @version 1.0
 * @description
 * @date 2025/11/16 11:31
 */
public class LoginUserContext {

    private static final ThreadLocal<LoginUserVO> USER_CONTEXT = new InheritableThreadLocal<>();

    public static LoginUserVO getUser() {
        return USER_CONTEXT.get();
    }

    public static void setUser(LoginUserVO loginUser) {
        USER_CONTEXT.set(loginUser);
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }

    public static String getUserId() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getId() : null;
    }

    public static String getUsername() {
        LoginUserVO loginUser = USER_CONTEXT.get();
        return loginUser != null ? loginUser.getUsername() : null;
    }
}
