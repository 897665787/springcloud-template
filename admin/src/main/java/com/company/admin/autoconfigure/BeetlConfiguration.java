package com.company.admin.autoconfigure;

import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import com.company.admin.tag.DescriptionOptionsTag;
import com.company.admin.tag.DictDesc2Tag;
import com.company.admin.tag.DictDescTag;
import com.company.admin.tag.DictOptionsTag;

/**
 * 实现 initOther方法,以注册自己的函数，标签等
 */
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Override
    public void initOther() {
		groupTemplate.registerTag("dictDesc", DictDescTag.class);
		groupTemplate.registerTag("dictDesc2", DictDesc2Tag.class);
		groupTemplate.registerTag("dictOptions", DictOptionsTag.class);
		groupTemplate.registerTag("descriptionOptions", DescriptionOptionsTag.class);
//		groupTemplate.registerTagFactory("", tagFactory);
		
//        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
//        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
//        groupTemplate.registerFunctionPackage("tool", new WebToolUtils());
//        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
//        groupTemplate.registerTagFactory("dictSelector", () -> dictSelectorTag);
/*
        groupTemplate.registerFunction("env", new Function() {
            @Override
            public String call(Object[] paras, Context ctx) {
                int length=2;
                String key = (String) paras[0];
                String value = env.getProperty(key);
                if (value != null) {
                    return getStr(value);
                }
                if (paras.length == length) {
                    return (String) paras[1];
                }
                return null;
            }

            String getStr(String str) {
                try {
                    return new String(str.getBytes("iso8859-1"), StandardCharsets.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        */
    }
}
