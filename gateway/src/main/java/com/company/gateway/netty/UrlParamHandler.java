package com.company.gateway.netty;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.company.gateway.context.SpringContextUtil;
import com.google.common.collect.Lists;

import cn.hutool.core.util.URLUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlParamHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			String url = request.uri();
			String newUrl = encodeParam(url);
			log.info("url:{},newUrl:{}", url, newUrl);
			request.setUri(newUrl);
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public boolean isSharable() {
		return true;
	}

	/**
	 * 使用urlencode替换特殊字符
	 * 
	 * @param url
	 * @return
	 */
	private String encodeParam(String url) {
		// 开关控制，在新逻辑有问题的情况下可以快速切回旧逻辑（如果经过生产环境验证无问题后可去掉该开关）
		boolean enable = SpringContextUtil.getBooleanProperty("template.encodeParam", true);
		if (!enable) {
			return url;
		}
		
		if (!url.contains("?")) {
			return url;
		}
		String[] up = StringUtils.split(url, "?");
		String paramsStr = up[1];

		String[] nvps = StringUtils.split(paramsStr, "&");
		List<String> paramList = Lists.newArrayList();
		for (String nvp : nvps) {
			String[] nv = StringUtils.split(nvp, "=");
			paramList.add(nv[0] + "=" + URLUtil.encodeAll(URLUtil.decode(nv[1])));
		}
		return up[0] + "?" + paramList.stream().collect(Collectors.joining("&"));
	}
}
