package com.company.admin.aspect;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xxw
 * @date 2018/9/26
 */
public interface Logable {

    void log(HttpServletRequest request, String operation, Object param, Object result, boolean success);
}
