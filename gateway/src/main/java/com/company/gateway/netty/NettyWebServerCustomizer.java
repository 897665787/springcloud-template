package com.company.gateway.netty;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelPipeline;
import reactor.netty.ConnectionObserver;
import reactor.netty.NettyPipeline;

@Component
public class NettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
	@Override
	public void customize(NettyReactiveWebServerFactory factory) {
		factory.addServerCustomizers(httpServer -> httpServer.observe((conn, state) -> {
			if (state == ConnectionObserver.State.CONNECTED) {
				ChannelPipeline pipeline = conn.channel().pipeline();
				/**
				 * <pre>
				 * 替换ChannelPipeline中的ChannelHandler HttpCodec，实现类为HttpServerCodec
				 * CustomHttpServerCodec代码复制自HttpServerCodec，最终修改了HttpObjectDecoder.splitInitialLine的逻辑
				 * </pre>
				 */
				pipeline.replace(NettyPipeline.HttpCodec, NettyPipeline.HttpCodec, new CustomHttpServerCodec());

				/**
				 * 处理url参数的特殊字符
				 */
				pipeline.addAfter(NettyPipeline.HttpCodec, "urlParamHandler", new UrlParamHandler());
			}
		}));
	}
}