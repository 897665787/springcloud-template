package com.company.admin.tag;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.service.security.SecResourceService;
import com.company.admin.service.security.SecStaffService;
import com.company.framework.context.SpringContextUtil;

import cn.hutool.core.io.FileUtil;

public class MenuTag extends SimpleTagSupport {
    
	private static SecStaffService secStaffService = SpringContextUtil.getBean("secStaffService");
	private static SecResourceService secResourceService = SpringContextUtil.getBean("secResourceService");

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        
    	String menu_item_tpl = FileUtil.readString("classpath:tpl/menu-item.jsp", Charset.forName("utf-8"));
		String menu_item_sub_tpl = FileUtil.readString("classpath:tpl/menu-item-sub.jsp", Charset.forName("utf-8"));
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		SecStaff secStaff = secStaffService.getByUsername(
				new SecStaff(((User) securityContextImpl.getAuthentication().getPrincipal()).getUsername()));
		
		List<SecResource> secResourceList = secResourceService.treeByStaffId(secStaff.getId());
		
		StringBuilder lv1sb = new StringBuilder();
		for (SecResource lv1 : secResourceList) {
			List<SecResource> childrens = lv1.getChildren();
			
			StringBuilder lv2sb = new StringBuilder();
			if(childrens != null){
				for (SecResource lv2 : childrens) {
					String menu_item_sub_text = menu_item_sub_tpl
							.replace("${active}", Objects.equals(lv2.getKey(), value) ? "active" : "")
							.replace("${lv2.name}", lv2.getName())
							.replace("${lv2.url}", Optional.ofNullable(lv2.getUrl()).orElse(""))
							;
					lv2sb.append(menu_item_sub_text);
				}
			}
			String menu_item_text = menu_item_tpl
					.replace("${lv1.name}", lv1.getName())
					.replace("${children}", lv2sb.toString())
					;
			lv1sb.append(menu_item_text);
		}
		
        out.println(lv1sb.toString());
    }
}
