package com.rpc.web;

import com.rpc.common.annotation.RpcConsumer;
import com.rpc.service.api.test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class testController {

    @RpcConsumer
    private test tes1;

    @GetMapping("test")
    public String test(){
        return "test";
    }

    @GetMapping("hello")
    public String test2(){
        return "hello";
    }
}
