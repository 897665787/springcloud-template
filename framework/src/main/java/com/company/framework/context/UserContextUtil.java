package com.company.framework.context;

import org.apache.commons.lang3.StringUtils;

public class UserContextUtil {
    private static final ThreadLocal<String> CURRENT_USER = new InheritableThreadLocal<>();

    public static void setCurrentUserId(String currentUserId) {
        CURRENT_USER.set(currentUserId);
    }

    public static String currentUserId() {
        String userId = HttpContextUtil.currentUserId();
        /**
         * 因为ThreadLocal的值是来自请求头的，所以这里优先使用请求头的，请求头找不到再使用ThreadLocal的
         * 为什么要这么设计？因为异步情况下子线程无法获取请求头的值，所以只能使用ThreadLocal的值
         */
        if (StringUtils.isNotBlank(userId)) {// 优先使用请求头的，请求头找不到再使用ThreadLocal的
            return userId;
        }
        return CURRENT_USER.get();
    }

    public static void remove() {
        CURRENT_USER.remove();
    }
}