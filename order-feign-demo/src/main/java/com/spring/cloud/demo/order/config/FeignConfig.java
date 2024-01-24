package com.spring.cloud.demo.order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义全局的日志
 */
//@Configuration
public class FeignConfig {

    /**
     * feign的日志配置
     * @return 日志级别
     */
    @Bean
    public Logger.Level feignLoggingLevel(){
        return Logger.Level.FULL;
    }
}
