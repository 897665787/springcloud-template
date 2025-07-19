package com.company.tool.nav.param;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.tool.nav.NavReplaceParam;
import com.google.common.collect.Maps;

/**
 * 榜单商品替换
 */
@Component("{bdgoods.")
public class BdGoodsReplaceParam implements NavReplaceParam {

	@Override
	public Map<String, String> replace(Map<String, String> attachMap) {
		Map<String, String> configParams = Maps.newHashMap();
		configParams.put("code}", "P131251651");
		configParams.put("name}", "小炒肉+青菜");
		configParams.put("image}", "http://sadas.com/iamge/aa.jpg");
		return configParams;
	}
}