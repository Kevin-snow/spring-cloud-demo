package com.spring.cloud.demo.store.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/subtract")
    public String subtractStore(){
        return "端口号"+ port +"，服务扣减库存";
    }

}
