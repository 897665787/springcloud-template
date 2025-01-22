package com.company.admin.tag;

import com.company.framework.context.SpringContextUtil;
import com.company.admin.service.security.SecResourceService;
import com.company.admin.entity.security.SecResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 获取指定资源所需的权限
 * Created by JQ棣 on 2017/11/8.
 */
public class PermissionTag extends SimpleTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(PermissionTag.class);

    private static SecResourceService secResourceService = SpringContextUtil.getBean("secResourceService");

    public static String getPermissions(String resourceKey) {
        try {
            SecResource secResource = secResourceService.getByKey(new SecResource(resourceKey));
            if (secResource != null) {
                return "\"ROLE_" + secResource.getKey() + "\"";
            }
        } catch (Exception e) {
            logger.error("error : ", e);
        }
        return "\"\"";
    }
}
