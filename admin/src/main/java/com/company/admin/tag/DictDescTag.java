package com.company.admin.tag;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.beetl.core.BeetlKit;
import org.beetl.core.Context;
import org.beetl.core.Tag;

import com.google.common.collect.Maps;

/**
 * 根据value查询desc的标签逻辑
 */
public class DictDescTag extends Tag {

	Map<String, String> kv = Maps.newHashMap();
	{
		kv.put("a", "a1");
		kv.put("b", "b1");
		kv.put("c", "c1");
		kv.put("d", "d1");
		kv.put("e", "e1");
	}
	
	@Override
	public void render() {
		StringBuilder html = new StringBuilder();
		Object[] args2 = args;
		Object tagName = args[0];
		Object attrs = args[1];
		Context ctx2 = ctx;
		Map<String, Object> globalVar = ctx2.globalVar;
		LinkedHashMap a = (LinkedHashMap) args[1];
		String desc = kv.get(a.get("key"));
		html.append("<div class=\"form-group\">");
		html.append(desc);
		html.append("<div>\r\n");
//		BeetlKit.render(template, paras)
		try {
			this.ctx.byteWriter.writeString(html.toString());
		} catch (IOException e) {
			throw new RuntimeException("输出字典标签错误");
		}
	}
	
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
}
