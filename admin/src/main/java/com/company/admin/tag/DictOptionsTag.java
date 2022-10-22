package com.company.admin.tag;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.company.framework.context.SpringContextUtil;
import com.company.admin.service.system.DictionaryService;
import com.google.common.collect.Sets;

/**
 * 根据字典数组输出<option></option>标签
 * Created by gustinlau on 11/2/17.
 */
public class DictOptionsTag extends SimpleTagSupport {

    private static DictionaryService dictionaryService = SpringContextUtil.getBean("dictionaryService");

    /**
     * 字典key
     */
    private String key;

    /**
     * 默认选择值
     */
    private String value;

    /**
     * 包含值，多个值使用英文逗号分隔
     */
    private String include;
    
    /**
     * 剔除值，多个值使用英文逗号分隔
     */
    private String exclude;
    
    public void setKey(String key) {
        this.key = key;
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
        Map<String, String> items = dictionaryService.mapByCategory(key);
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
