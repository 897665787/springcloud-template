package com.company.admin.tag;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.GeneralVarTagBinding;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据clazz查询property的@AutoDesc枚举，列举所有数据组装<option></option>返回
 */
@Slf4j
public class DescriptionOptionsTag extends GeneralVarTagBinding {

	@Override
	public void render() {
		StringBuilder html = new StringBuilder();

		Map<String, Object> attrs = this.getAttributes();
		String clazzStr = MapUtils.getString(attrs, "clazz");
		String property = MapUtils.getString(attrs, "property");
		String value = MapUtils.getString(attrs, "value");
		String include = MapUtils.getString(attrs, "include");
		String exclude = MapUtils.getString(attrs, "exclude");

		if (StringUtils.isBlank(clazzStr)) {
			html.append("'clazz' not null");
			try {
				this.ctx.byteWriter.writeString(html.toString());
			} catch (IOException e) {
				throw new RuntimeException("输出字典标签错误");
			}
		}
		
		Class<?> clazz = null;
		try {
			clazz = Class.forName(clazzStr);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
		if (clazz != null) {
			Map<String, String> items = DescriptionUtils.descriptions(clazz, property);
			if (items != null && items.size() > 0) {
				for (Map.Entry<String, String> entry : items.entrySet()) {
					if (include != null) {
						Set<String> includeSet = Sets.newHashSet(include.split(","));
						if (!includeSet.contains(entry.getKey())) {
							continue;
						}
					}

					if (exclude != null) {
						Set<String> excludeSet = Sets.newHashSet(exclude.split(","));
						if (excludeSet.contains(entry.getKey())) {
							continue;
						}
					}

					if (entry.getKey().equals(value)) {
						html.append(
								"<option value=\"" + entry.getKey() + "\" selected>" + entry.getValue() + "</option>");
					} else {
						html.append("<option value=\"" + entry.getKey() + "\">" + entry.getValue() + "</option>");
					}

				}
			}
		}

		try {
			this.ctx.byteWriter.writeString(html.toString());
		} catch (IOException e) {
			throw new RuntimeException("输出字典标签错误");
		}
	}
}
