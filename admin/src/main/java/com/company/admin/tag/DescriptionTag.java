package com.company.admin.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.admin.util.DescriptionUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * @author JQ棣
 * @date 2018/10/8
 */
public class DescriptionTag extends SimpleTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionTag.class);

    private String clazz;

    private String property;

    private String value;

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        try {
            Class<?> clazz = Class.forName(this.getClass().getPackage().getName().replace("admin.tag", "admin.entity") + "." + this.clazz);
            out.println(DescriptionUtils.description(clazz, property, value));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }
}
