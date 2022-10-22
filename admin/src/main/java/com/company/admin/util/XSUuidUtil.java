package com.company.admin.util;

import java.util.UUID;

/**
 * Uuid生成工具
 * Created by xuxiaowei on 2017/10/20.
 */
public class XSUuidUtil {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
