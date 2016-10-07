package com.jmelzer.jitty.logging;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * Created by J. Melzer on 21.08.2016.
 * Proof if the size of a rest request os to large and loggin it.
 */

@Component
public class ResponseSizeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        OutputStream out = response.getOutputStream();
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, wrapper);
        byte responseContent[] = wrapper.getData();

        // Write copied response, should change whatever you want in 'responseContent' before sending to client
        out.write(responseContent);

        // Finished.
        out.close();
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        if (url.contains("/api")) {
            if (responseContent.length > 1000) {
                System.err.println("response too large: " + responseContent.length + " for api call " + url);
            }
        }
    }

    @Override
    public void destroy() {

    }

    class CopyServletOutputStream extends ServletOutputStream {

        private DataOutputStream stream;

        public CopyServletOutputStream(OutputStream output) {
            stream = new DataOutputStream(output);
        }

        public void write(int b) throws IOException {
            stream.write(b);
        }

        public void write(byte[] b) throws IOException {
            stream.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            stream.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }


    class ResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream output;
        private String contentType;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteArrayOutputStream();
        }

        public byte[] getData() {
            return output.toByteArray();
        }

        public ServletOutputStream getOutputStream() {
            return new CopyServletOutputStream(output);
        }

        public PrintWriter getWriter() {
            return new PrintWriter(getOutputStream(), true);
        }

        public void setContentType(String type) {
            this.contentType = type;
            super.setContentType(type);
        }


        public String getContentType() {
            return contentType;
        }
    }
}
