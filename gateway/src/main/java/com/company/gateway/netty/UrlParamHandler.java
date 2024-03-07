package com.company.gateway.netty;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
		if (!url.contains("?")) {
			return url;
		}
		String[] up = StringUtils.split(url, "?", 2);
		if (up.length != 2) {
			return url;
		}
		String paramsStr = up[1];

		String[] nvps = StringUtils.split(paramsStr, "&");
		List<String> paramList = Lists.newArrayList();
		for (String nvp : nvps) {
			String[] nv = StringUtils.split(nvp, "=", 2);
			if (nv.length == 2) {
				paramList.add(nv[0] + "=" + URLUtil.encodeAll(URLUtil.decode(nv[1])));
			} else {
				paramList.add(nvp);
			}
		}
		return up[0] + "?" + paramList.stream().collect(Collectors.joining("&"));
	}
}
