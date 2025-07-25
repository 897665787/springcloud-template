package com.company.framework.context;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 当前登录用户上下文
 */
public class UserContextUtil {
    private static final ThreadLocal<String> CURRENT_USER = new InheritableThreadLocal<>();

    public static void setCurrentUserId(String currentUserId) {
        CURRENT_USER.set(currentUserId);
    }

    public static String currentUserId() {
        /**
         * 因为ThreadLocal的值是来自请求头的，所以这里优先使用请求头的，请求头找不到再使用ThreadLocal的
         * 为什么要这么设计？因为异步情况下子线程无法获取请求头的值，所以只能使用ThreadLocal的值
         */
        String userId = HttpContextUtil.currentUserId();
        if (StringUtils.isNotBlank(userId)) {
            return userId;
        }
        return CURRENT_USER.get();
    }

    public static Integer currentUserIdInt() {
        return Optional.ofNullable(currentUserId()).map(Integer::valueOf).orElse(null);
    }

    public static void remove() {
        CURRENT_USER.remove();
    }
}