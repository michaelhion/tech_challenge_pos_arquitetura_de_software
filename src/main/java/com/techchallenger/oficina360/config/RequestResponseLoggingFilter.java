package com.techchallenger.oficina360.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    private static final int MAX_PAYLOAD_LENGTH = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();

        MDC.put("requestId", requestId);

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 10_000);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {

            long duration = System.currentTimeMillis() - startTime;

            logRequest(requestWrapper, requestId);
            logResponse(responseWrapper, duration, requestId);

            responseWrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) {

        String body = getBody(request.getContentAsByteArray());
        body = sanitizePayload(body);

        String user = getUsuarioLogado();

        log.info("""
                
                ======== HTTP REQUEST ========
                requestId : {}
                method    : {}
                uri       : {}{}
                user      : {}
                ip        : {}
                payload   : {}
                ==============================
                """, requestId, request.getMethod(), request.getRequestURI(), getQueryString(request), user, request.getRemoteAddr(), body);
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration, String requestId) {

        String body = getBody(response.getContentAsByteArray());
        body = sanitizePayload(body);

        log.info("""
                
                ======== HTTP RESPONSE ========
                requestId : {}
                status    : {}
                duration  : {} ms
                payload   : {}
                ==============================
                """, requestId, response.getStatus(), duration, body);
    }

    private String getBody(byte[] content) {
        if (content.length == 0) {
            return "[empty]";
        }

        String body = new String(content, StandardCharsets.UTF_8);

        if (body.length() > MAX_PAYLOAD_LENGTH) {
            return body.substring(0, MAX_PAYLOAD_LENGTH) + "...[truncated]";
        }

        return body;
    }

    private String sanitizePayload(String body) {

        if (body == null) return null;

        body = body.replaceAll("\\b\\d{11}\\b", "***CPF***");

        body = body.replaceAll("\\b\\d{14}\\b", "***CNPJ***");

        body = body.replaceAll("(?i)\"senha\"\\s*:\\s*\"[^\"]*+\"","\"senha\":\"***\""
        );

        body = body.replaceAll("(?i)\"token\"\\s*:\\s*\"[^\"]*+\"","\"token\":\"***\""
        );

        return body;
    }

    private String getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return "anonymous";
        }

        return authentication.getName();
    }

    private String getQueryString(HttpServletRequest request) {
        return request.getQueryString() != null ? "?" + request.getQueryString() : "";
    }
}