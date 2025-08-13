package com.cinematch.cinematch.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class HttpsRedirectConfig {

    @Bean
    public FilterRegistrationBean<HttpsRedirectFilter> httpsRedirectFilter() {
        FilterRegistrationBean<HttpsRedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpsRedirectFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    public static class HttpsRedirectFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String forwardedProto = httpRequest.getHeader("X-Forwarded-Proto");

            if ("http".equals(forwardedProto)) {
                String httpsUrl = "https://" + httpRequest.getServerName() + httpRequest.getRequestURI();
                if (httpRequest.getQueryString() != null) {
                    httpsUrl += "?" + httpRequest.getQueryString();
                }

                httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpResponse.setHeader("Location", httpsUrl);
                return;
            }

            chain.doFilter(request, response);
        }
    }
}