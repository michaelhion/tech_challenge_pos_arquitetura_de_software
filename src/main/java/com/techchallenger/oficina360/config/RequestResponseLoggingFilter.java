package com.techchallenger.oficina360.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Wrap request and response to enable body caching
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request,10);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // Proceed with the request execution down the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Extract data and perform logging
            logRequest(requestWrapper);
            logResponse(responseWrapper, duration);

            // CRITICAL: Copy content back to the original response stream so the client receives it
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = getStringValue(request.getContentAsByteArray(), request.getCharacterEncoding());
        logger.info("--- HTTP REQUEST ---");
        logger.info("Method      : {}", request.getMethod());
        logger.info("URI         : {}", request.getRequestURI());
        logger.info("Query String: {}", request.getQueryString());
        logger.info("Payload     : {}", body.strip());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        String body = getStringValue(response.getContentAsByteArray(), response.getCharacterEncoding());
        logger.info("--- HTTP RESPONSE ---");
        logger.info("Status      : {}", response.getStatus());
        logger.info("Duration    : {} ms", duration);
        logger.info("Payload     : {}", body.strip());
    }

    private String getStringValue(byte[] content, String contentEncoding) {
        try {
            if (content.length == 0) {
                return "[empty]";
            }
            return new String(content, contentEncoding != null ? contentEncoding : "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "[unknown encoding]";
        }
    }
}

