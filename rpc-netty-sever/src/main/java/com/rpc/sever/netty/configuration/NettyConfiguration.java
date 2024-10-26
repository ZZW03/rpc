package com.rpc.sever.netty.configuration;

import com.rpc.sever.netty.sever.NettySever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class NettyConfiguration {

    @Bean
    @Lazy
    public NettySever nettySever(){
        return new NettySever();
    }
}
