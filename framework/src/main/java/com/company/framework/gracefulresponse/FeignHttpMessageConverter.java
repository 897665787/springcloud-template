package com.company.framework.gracefulresponse;

import com.feiniaojin.gracefulresponse.data.Response;
import com.google.common.collect.Lists;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

/**
 * feign调用过程中传递header值
 *
 * 依赖Hystrix自定义并发策略:TransferHystrixConcurrencyStrategy
 */
//@Configuration
public class FeignHttpMessageConverter implements HttpMessageConverter<Response> {
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON);
	}

	@Override
	public Response read(Class<? extends Response> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override
	public void write(Response response, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		System.out.println("FeignHttpMessageConverter write method called");
	}
}