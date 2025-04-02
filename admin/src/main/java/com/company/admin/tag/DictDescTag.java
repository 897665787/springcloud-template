package com.company.admin.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.company.framework.context.SpringContextUtil;
import com.company.admin.service.system.DictionaryService;

/**
 * 根据value查询desc的标签逻辑
 * Created by JQ棣 on 11/2/17.
 */
public class DictDescTag extends SimpleTagSupport {

    private static DictionaryService dictionaryService = SpringContextUtil.getBean("dictionaryService");

    /**
     * 字典的Key
     */
    private String key;

    /**
     * 查询的值
     */
    private String value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String desc = dictionaryService.findByKey(key, value).orElse("");
        if (desc != null) {
            out.println(desc);
        }
    }
}
