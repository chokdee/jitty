package com.jmelzer.jitty.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Config for spring security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // csrfTokenRepository is inaccessible in CsrfConfigurer so we create our own instance
    // and we give it to the CsrfTokenGeneratorFilter
    private final CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
    @Autowired
    ExternalAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authenticationProvider(authenticationProvider).httpBasic().and().authorizeRequests()
//                .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll().anyRequest()
//                .authenticated()
//                .and().csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/h2-console**"))
//                .csrfTokenRepository(csrfTokenRepository()).and().logout().logoutUrl("/logout").and()
//                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
        http.authorizeRequests().antMatchers("/").permitAll().and()

                .authorizeRequests().antMatchers("/console/**").permitAll();

        http.csrf().disable();

        http.headers().frameOptions().disable();
    }

    private Filter csrfHeaderFilter() {
        RequestMatcher ignoreMatcher = new AntPathRequestMatcher("/logout", "POST");
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                        .getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//                return ignoreMatcher.matches(request);
                return true;
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.debug(true);
        web.ignoring().antMatchers("/h2-console**", "/index.html", "/home.html", "/login.html", "/js/**", "/css/**", "/images/**", "/");
    }

}
