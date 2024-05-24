package com.dev.gbk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.dev.gbk.constant.GbkConstant;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Wrap the request and response to enable repeated reading
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        // Log request details
        logRequestDetails(wrappedRequest);

        // Proceed with the next filter in the chain
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        // Log response details
        logResponseDetails(wrappedResponse, wrappedRequest.getRequestURI());

        // Copy the response content to the original response
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException {
        logger.info("Request: method={} | path={} |",
                request.getMethod(),
                request.getRequestURI());
    }

    private void logResponseDetails(ContentCachingResponseWrapper response, String path) throws IOException {
        String body = maskSensitiveFields(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
        logger.info("Response: path={} | status={} | body={} |",
                path,
                response.getStatus(),
                body);
    }

    private String maskSensitiveFields(String data) {
        for (String field : GbkConstant.FIELDS_TO_MASK) {
            if (!StringUtils.isEmpty(data)) {
                data = data.replaceAll("\"" + field + "\"\\s*:\\s*\"[^\"]+\"", "\"" + field + "\":\"*\"");
            }
        }
        return data;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
