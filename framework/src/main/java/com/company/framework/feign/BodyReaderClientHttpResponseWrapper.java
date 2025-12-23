package com.company.framework.feign;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class BodyReaderClientHttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse response;
    private byte[] body;

    public BodyReaderClientHttpResponseWrapper(ClientHttpResponse response) {
        this.response = response;
    }

    public HttpStatus getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    public int getRawStatusCode() throws IOException {
        return this.response.getRawStatusCode();
    }

    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }

        return new ByteArrayInputStream(this.body);
    }

    public void close() {
        this.response.close();
    }
}