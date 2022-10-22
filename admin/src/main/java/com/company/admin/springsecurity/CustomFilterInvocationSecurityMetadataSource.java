package com.company.admin.springsecurity;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import com.company.admin.entity.security.SecResource;
import com.company.admin.service.security.SecResourceService;

/**
 * 系统用户请求拦截Service
 */
@Service("securityMetadataSource")
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);

    @Autowired
    private SecResourceService secResourceService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url = ((FilterInvocation) object).getRequestUrl();
        String method = ((FilterInvocation) object).getRequest().getMethod();
        //去除请求中的参数
        int firstQuestionMarkIndex = url.lastIndexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        //去除请求末尾多余的"/"
        int firstEndSlashIndex = -1;
        for (int i = url.length() - 1; i >= 0; --i) {
            if (url.charAt(i) == '/') {
                firstEndSlashIndex = i;
            }
            else {
                break;
            }
        }
        if (firstEndSlashIndex != -1) {
            url = url.substring(0, firstEndSlashIndex);
        }
        Collection<ConfigAttribute> attributes = new ArrayList<>();
        try {
            SecResource secResource = secResourceService.getByRequest(new SecResource(url, method));
            if (secResource != null) {
                attributes.add(new SecurityConfig("ROLE_" + secResource.getKey()));
            }
        } catch (Exception e) {
            logger.error("error : ", e);
        }
        if (attributes.size() == 0) {
            logger.info("请求{}没有被SpringSecurity控制起来，如需权限控制请在管理后台添加相应的系统资源", url + "(" + method + ")");
		}
		// 返回需要检验的权限，空列表代表不需要校验权限
		return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
