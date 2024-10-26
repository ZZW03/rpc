package com.rpc;


import com.rpc.netty.NettyClient;
import com.rpc.properties.SpringRpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NettyConfiguration {

    @Autowired
    SpringRpcProperties properties;


    @Bean(initMethod = "Start")
    public NettyClient nettyClient(){
        return new NettyClient(properties.getServiceAddress(), properties.getNettyPort(), properties.getAppPort());
    }

}
