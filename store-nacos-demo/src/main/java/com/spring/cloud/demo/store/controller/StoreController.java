package com.spring.cloud.demo.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    @GetMapping("/store/subtract")
    public String subtractStore(){
        return "扣减库存";
    }

}
