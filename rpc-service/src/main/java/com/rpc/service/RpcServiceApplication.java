package com.rpc.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.rpc")
public class RpcServiceApplication  {
    public static void main(String[] args) {
        SpringApplication.run(RpcServiceApplication.class, args);
    }
}
