package com.jmelzer.jitty.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bsmk on 8/24/14.
 */
public class CsrfTokenGeneratorFilter extends OncePerRequestFilter {

    public static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";

    private final CsrfTokenRepository repository;
    private final RequestMatcher ignoreMatcher;

    public CsrfTokenGeneratorFilter(CsrfTokenRepository repository, RequestMatcher ignoreMatcher) {
        this.repository = repository;
        this.ignoreMatcher = ignoreMatcher;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return ignoreMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // we generate new token on every request
        CsrfToken token = repository.generateToken(request);
        repository.saveToken(token, request, response);

        // Spring Security will allow the Token to be included in this header name
        response.setHeader("X-CSRF-HEADER", token.getHeaderName());

        // Spring Security will allow the token to be included in this parameter name
        response.setHeader("X-CSRF-PARAM", token.getParameterName());

        // this is the value of the token to be included as either a header or an HTTP parameter
        response.setHeader(CSRF_TOKEN_HEADER, token.getToken());

        filterChain.doFilter(request, response);
    }

}
