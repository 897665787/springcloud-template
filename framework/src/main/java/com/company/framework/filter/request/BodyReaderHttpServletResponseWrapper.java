package com.company.framework.filter.request;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class BodyReaderHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedOutputStream = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public BodyReaderHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (outputStream == null) {
            outputStream = new ServletOutputStream() {
                private final ServletOutputStream originalOutputStream = getResponse().getOutputStream();

                @Override
                public void write(int b) throws IOException {
                    cachedOutputStream.write(b);     // 缓存响应体
                    originalOutputStream.write(b);   // 写入原始响应流
                }

                @Override
                public boolean isReady() {
                    return originalOutputStream.isReady();
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    originalOutputStream.setWriteListener(writeListener);
                }
            };
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }

        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(cachedOutputStream, getCharacterEncoding())) {
                private final PrintWriter originalWriter = getResponse().getWriter();

                @Override
                public void write(char[] buf, int off, int len) {
                    super.write(buf, off, len);    // 写入缓存
                    originalWriter.write(buf, off, len);  // 写入原始响应流
                }

                @Override
                public void flush() {
                    super.flush();
                    originalWriter.flush();
                }
            };
        }
        return writer;
    }

    /**
     * 获取缓存的响应体字节数据
     */
    public byte[] getCachedBody() {
        if (outputStream != null) {
            return cachedOutputStream.toByteArray();
        } else if (writer != null) {
            try {
                writer.flush();  // 确保缓冲区数据写入缓存
                return cachedOutputStream.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Failed to flush writer cache", e);
            }
        }
        return new byte[0];
    }

    /**
     * 获取缓存的响应体字符串（需确保编码正确）
     */
    public String getCachedBodyString() {
        return new String(getCachedBody(), StandardCharsets.UTF_8);
    }
}
