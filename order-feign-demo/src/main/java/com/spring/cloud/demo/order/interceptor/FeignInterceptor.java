package com.spring.cloud.demo.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignInterceptor implements RequestInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.uri("/subtract/1");
        logger.info("自定义拦截器");
    }
}
