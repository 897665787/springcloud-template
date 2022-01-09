package com.company.admin.tag;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.beetl.core.GeneralVarTagBinding;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 根据字典数组输出<option></option>标签
 */
public class DictOptionsTag extends GeneralVarTagBinding {

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
		
		String htmlTagName = this.getHtmlTagName();
		Map<String, Object> items = this.getAttributes();
		
		String value = MapUtils.getString(items, "value");
		String include = MapUtils.getString(items, "include");
		String exclude = MapUtils.getString(items, "exclude");
		
        for (Map.Entry<String, String> entry : kv.entrySet()) {
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
            	html.append("<option value=\"" + entry.getKey() + "\" selected>" + entry.getValue() + "</option>");
            }else{
            	html.append("<option value=\"" + entry.getKey() + "\">" + entry.getValue() + "</option>");
            }

        }
        
		try {
			this.ctx.byteWriter.writeString(html.toString());
		} catch (IOException e) {
			throw new RuntimeException("输出字典标签错误");
		}
	}
    
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
	/*
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

*/

}
