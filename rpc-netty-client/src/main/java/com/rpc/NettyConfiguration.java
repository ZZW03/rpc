package com.rpc;

import com.rpc.client.SpringRpcClientAutoProxy;
import com.rpc.common.netty.model.SpringRpcProperties;
import com.rpc.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfiguration {




    @Bean
    public NettyClient nettyClient(@Qualifier("springRpcProperties") SpringRpcProperties properties){
        return new NettyClient(properties.getServiceAddress(), properties.getNettyPort());
    }

    @Bean
    public SpringRpcClientAutoProxy springRpcClientAutoProxy(@Qualifier("springRpcProperties") SpringRpcProperties properties){
        return new SpringRpcClientAutoProxy(properties.getAppPort());
    }



}
