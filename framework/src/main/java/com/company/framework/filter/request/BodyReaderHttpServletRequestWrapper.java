package com.company.framework.filter.request;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;
	private String bodyStr;

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		String bodyString = getBodyString(request);
		body = bodyString.getBytes(StandardCharsets.UTF_8);
		bodyStr = bodyString;
	}

	public String getBodyString(HttpServletRequest request) throws IOException {
		try (InputStream inputStream = request.getInputStream()) {
			String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			// body 有换行和空格，暂时没想到好的办法处理
			return body;
		}
	}

	public String getBodyStr() {
		return bodyStr;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}

}
