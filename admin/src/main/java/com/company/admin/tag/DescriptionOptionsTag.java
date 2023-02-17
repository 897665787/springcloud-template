package com.company.admin.tag;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.admin.util.DescriptionUtils;
import com.google.common.collect.Sets;

/**
 * @author xxw
 * @date 2018/10/8
 */
public class DescriptionOptionsTag extends SimpleTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionOptionsTag.class);

    private String clazz;

    private String property;

    private String value;

    /**
     * 包含值，多个值使用英文逗号分隔
     */
    private String include;
    
    /**
     * 剔除值，多个值使用英文逗号分隔
     */
    private String exclude;
    
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public void setInclude(String include) {
		this.include = include;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}
	
    @Override
    public void doTag() throws JspException, IOException {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(this.getClass().getPackage().getName().replace("admin.tag", "admin.entity") + "." + this.clazz);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        if (clazz != null) {
            Map<String, String> items = DescriptionUtils.descriptions(clazz, property);
            if (items != null && items.size() > 0) {
                JspWriter out = getJspContext().getOut();
                for (Map.Entry<String, String> entry : items.entrySet()) {
                	if(include != null){
                		Set<String> includeSet = Sets.newHashSet(include.split(","));
                		if(!includeSet.contains(entry.getKey())){
                			continue;
                		}
                	}
                	
                	if(exclude != null){
                		Set<String> excludeSet = Sets.newHashSet(exclude.split(","));
                		if(excludeSet.contains(entry.getKey())){
                			continue;
                		}
                	}
                	
                    if(entry.getKey().equals(value)){
                        out.println("<option value=\"" + entry.getKey() + "\" selected>" + entry.getValue() + "</option>");
                    }else{
                        out.println("<option value=\"" + entry.getKey() + "\">" + entry.getValue() + "</option>");
                    }

                }
            }
        }
    }
}
