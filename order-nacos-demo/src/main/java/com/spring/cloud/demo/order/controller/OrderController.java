package com.spring.cloud.demo.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/order/add")
    public String addOrder(){
        //在不使用spring-cloud注册中心的时候，如果想调用另外一个服务的接口时，需要知道被调用服务接口准确的url.
        String msg = restTemplate.getForObject("http://localhost:8202/store/subtract", String.class);
        return "下单成功！" + msg;
    }

}
