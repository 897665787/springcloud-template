package com.company.framework.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.company.framework.constant.HeaderConstants;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 请求头放到上下文
 */
public class Header2ContextUtil {
    // 有问题：父子线程不能传递
    // private static final ThreadLocal<String> CURRENT_TL = new ThreadLocal<>();

    // 有问题：子线程初始化后不能修改值
    // private static final ThreadLocal<String> CURRENT_TL = new InheritableThreadLocal<>();

    // 启动参数加上：-javaagent:path/to/transmittable-thread-local-2.14.5.jar，否则问题同InheritableThreadLocal
    private static final ThreadLocal<Map<String, String>> CURRENT_TL = new TransmittableThreadLocal<>();

    public static void setHeaderMap(Map<String, String> headerMap) {
        CURRENT_TL.set(headerMap);
    }

    public static String currentUserId() {
        return head(HeaderConstants.HEADER_CURRENT_USER_ID);
    }

    public static String deviceid() {
        return head(HeaderConstants.HEADER_DEVICEID);
    }

    public static String channel() {
        return head(HeaderConstants.HEADER_CHANNEL);
    }

    public static String head(String name) {
        return Optional.ofNullable(headerMap()).map(m -> m.get(name)).orElse(null);
    }

    public static Map<String, String> headerMap() {
        /**
         * 因为ThreadLocal的值是来自请求头的，所以这里优先使用请求头的，请求头找不到再使用ThreadLocal的
         * 为什么要这么设计？因为异步情况下子线程无法获取请求头的值，所以只能使用ThreadLocal的值
         */
        Map<String, String> headerMap = HttpContextUtil.httpContextHeader();
        if (MapUtils.isNotEmpty(headerMap)) {
            return headerMap;
        }
        return CURRENT_TL.get();
    }

    public static void remove() {
        CURRENT_TL.remove();
    }
}
