package com.company.admin.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import com.company.admin.tag.DescriptionOptionsTag;
import com.company.admin.tag.DictDesc2Tag;
import com.company.admin.tag.DictDescTag;
import com.company.admin.tag.DictOptionsTag;
import com.company.admin.util.Utils;
import com.company.common.util.JsonUtil;
import com.company.framework.context.SpringContextUtil;

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
		
		groupTemplate.registerFunctionPackage("utils", new Utils());
		groupTemplate.registerFunctionPackage("stringUtils", StringUtils.class);
		
//		groupTemplate.registerTagFactory("", tagFactory);
		
//        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
//        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
//        groupTemplate.registerFunctionPackage("tool", new WebToolUtils());
//        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
//        groupTemplate.registerTagFactory("dictSelector", () -> dictSelectorTag);

        groupTemplate.registerFunction("env", new Function() {
            @Override
            public String call(Object[] paras, Context ctx) {
                System.out.println(JsonUtil.toJsonString(paras));
                return SpringContextUtil.getActiveProfile();
            }
        });
        
        groupTemplate.registerFunction("isDev", new Function() {
        	@Override
        	public Boolean call(Object[] paras, Context ctx) {
        		System.out.println(JsonUtil.toJsonString(paras));
        		return SpringContextUtil.getActiveProfile().equals("dev");
        	}
        });
        
    }
}
