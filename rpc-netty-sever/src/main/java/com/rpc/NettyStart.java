package com.rpc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.rpc.sever", "com.register"})
public class NettyStart {
    public static void main(String[] args) {
        SpringApplication.run(NettyStart.class, args);
    }
}
