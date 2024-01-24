package com.spring.cloud.demo.store.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {


    @Value("${server.port}")
    private int port;

    @GetMapping("/subtract/{id}")
    public String subtractMember(@PathVariable("id") String id){
        return "port:"+ port +"，扣减"+ id + "号会员余额";
    }

}
