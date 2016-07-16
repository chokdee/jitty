package com.jmelzer.jitty.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.servlet.Filter;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Configuration
public class JittyConfig {
//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(inViewVilter());
//        registration.addUrlPatterns("/api/*");
//        registration.setName("inViewVilter");
//        registration.setOrder(1);
//        return registration;
//    }
//
//    @Bean(name = "inViewVilter")
//    public Filter inViewVilter() {
//        return new OpenEntityManagerInViewFilter();
//    }
}
