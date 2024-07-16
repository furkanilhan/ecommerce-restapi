package com.furkan.ecommerce.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class RequestResponseLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URL: {}, Method: {}, IP Address: {}, User-Agent: {}",
                request.getRequestURL(), request.getMethod(), request.getRemoteAddr(), request.getHeader("User-Agent"));

        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null && !paramMap.isEmpty()) {
            StringBuilder params = new StringBuilder("Request Parameters:");
            paramMap.forEach((key, values) -> {
                for (String value : values) {
                    params.append(" ").append(key).append("=").append(value);
                }
            });
            logger.info(params.toString());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Response Status: {}", response.getStatus());
    }
}

