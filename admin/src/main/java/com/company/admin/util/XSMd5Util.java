package com.company.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Md5加解密工具
 * Created by xuxiaowei on 2017/10/20.
 */
public class XSMd5Util {

    private static final Logger logger = LoggerFactory.getLogger(XSMd5Util.class);

    public static String encode(String plaintext) {
    	return new BCryptPasswordEncoder().encode(plaintext);
//        try {
//            return DigestUtils.md5Hex(plaintext.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            logger.error("error: ", e);
//            throw new RuntimeException(e);
//        }
    }
}
