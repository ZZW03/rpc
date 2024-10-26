package com.rpc.service;

import com.register.nacos.model.RpcServiceInstance;
import com.register.nacos.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class Test {

    @Autowired
    RegisterService registerService;

    @Value("${spring.application.name}")
    private String name;

    @PostMapping("/test")
    public void test(String name,@RequestBody RpcServiceInstance rpcServiceInstance)  {
        registerService.register(name,rpcServiceInstance);
    }

    @GetMapping("find")
    public void findByName(String name){
        System.out.println(registerService.findAllByServiceName(name));
    }


}
